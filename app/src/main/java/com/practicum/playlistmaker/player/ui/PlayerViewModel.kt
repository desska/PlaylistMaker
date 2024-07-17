package com.practicum.playlistmaker.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.newlist.domain.entity.Playlist
import com.practicum.playlistmaker.player.domain.PlayerInteractor
import com.practicum.playlistmaker.player.domain.entity.BottomSheetState
import com.practicum.playlistmaker.player.domain.entity.PlayerState
import com.practicum.playlistmaker.player.domain.entity.Track
import com.practicum.playlistmaker.playlist.domain.PlaylistInteractor
import com.practicum.playlistmaker.playlist.domain.entity.PlaylistErrorType
import com.practicum.playlistmaker.playlist.domain.entity.PlaylistState
import com.practicum.playlistmaker.player.domain.entity.ToastState
import com.practicum.playlistmaker.utils.SingleEventLiveData
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Date

class PlayerViewModel(
    val track: Track,
    private val playerInteractor: PlayerInteractor,
    private val playlistInteractor: PlaylistInteractor
) :
    ViewModel() {
    private val toastState = SingleEventLiveData<ToastState>()
    fun observeToastState(): LiveData<ToastState> = toastState

    private val bottomSheetState = SingleEventLiveData<BottomSheetState>()
    fun observeBottomSheetState(): LiveData<BottomSheetState> = bottomSheetState

    private val playerState = MutableLiveData<PlayerState>(PlayerState.Default)
    fun observePlayerState(): LiveData<PlayerState> = playerState

    private val progress = MutableLiveData(0)
    fun getProgress(): LiveData<Int> = progress

    private val isFavoriteState = MutableLiveData(false)
    fun getIsFavoriteState(): LiveData<Boolean> = isFavoriteState

    private var timerJob: Job? = null
    private val listState =
        MutableLiveData<PlaylistState>()

    fun observeListState(): LiveData<PlaylistState> = listState

    fun fillPlaylistData() {
        viewModelScope.launch {
            playlistInteractor.getLists()
                .collect {
                    process(it)
                }
        }
    }

    private fun process(lists: List<Playlist>) {
        if (lists.isNotEmpty()) {
            listState.postValue(PlaylistState.Content(lists))
        } else {
            listState.postValue(PlaylistState.Error(PlaylistErrorType.EMPTY_PLAYLIST))
        }
    }

    fun onPlaylistClick(playlist: Playlist, track: Track) {
        addToPlaylist(playlist, track)
    }

    private fun addToPlaylist(playlist: Playlist, track: Track) {
        if (track.trackId == null) {
            return
        }

        viewModelScope.launch {
            playerInteractor.isInPlaylist(track.trackId, playlist.id).collect {
                if (it) {
                    toastState.postValue(ToastState.IsInList(playlist.name))
                } else {
                    playerInteractor.addToPlaylist(track, playlist.id)
                    toastState.postValue(ToastState.IsAdded(playlist.name))
                    fillPlaylistData()
                    hideBottomSheet()
                }
            }
        }
        fillPlaylistData()
    }

    init {
        val url = track.previewUrl ?: ""

        if (url == "") {
            playerState.postValue(PlayerState.Error("url error"))

        } else {
            playerInteractor.prepare(url, object : PlayerInteractor.OnPreparedListener {
                override fun onPrepared() {
                    playerState.value = PlayerState.Paused
                }

                override fun onComplete() {
                    playerState.value = PlayerState.Paused
                    timerJob?.cancel()
                    progress.value = 0
                }

            })

        }

        viewModelScope.launch {
            playerInteractor.isInFavorite(track.trackId).collect {
                isFavoriteState.postValue(it)
            }
        }

    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (playerState.value is PlayerState.Playing) {
                delay(REFRESH_TIME_MS)
                progress.value = playerInteractor.getCurrentPosition()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()

        release()
    }

    fun toggleFavorite() {
        if (isFavoriteState.value == false) {
            addToFavorite()
        } else {
            removeFromFavorite()
        }
    }

    fun onPlaybackControl() {
        when (playerState.value) {
            is PlayerState.Playing -> {
                pause()
            }

            is PlayerState.Paused -> {
                start()
            }

            else -> {}
        }
    }

    fun onPause(): Unit = pause()

    private fun start() {
        if (playerState.value is PlayerState.Error) return
        playerInteractor.start()
        playerState.value = PlayerState.Playing
        startTimer()
    }

    private fun pause() {
        if (playerState.value is PlayerState.Error) return
        playerInteractor.pause()
        playerState.value = PlayerState.Paused
        timerJob?.cancel()
    }

    private fun release() {
        if (playerState.value is PlayerState.Error) return
        playerInteractor.release()
        playerState.value = PlayerState.Paused

    }

    companion object {
        private const val REFRESH_TIME_MS = 300L

    }

    private fun addToFavorite() {
        viewModelScope.launch {
            playerInteractor.addToFavorite(track = track, Date())
        }
        isFavoriteState.postValue(true)

    }

    private fun removeFromFavorite() {
        viewModelScope.launch {
            playerInteractor.removeFromFavorite(trackId = track.trackId)
        }
        isFavoriteState.postValue(false)
    }

    fun onAddPlaylistClick() {
        collapseBottomSheet()
    }

    private fun hideBottomSheet() {
       bottomSheetState.postValue(BottomSheetState.Hidden)
    }

    private fun collapseBottomSheet() {
        bottomSheetState.postValue(BottomSheetState.Collapsed)
    }

}