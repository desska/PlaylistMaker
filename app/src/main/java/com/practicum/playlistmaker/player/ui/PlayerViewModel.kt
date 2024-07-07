package com.practicum.playlistmaker.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.player.domain.PlayerInteractor
import com.practicum.playlistmaker.player.domain.entity.PlayerState
import com.practicum.playlistmaker.player.domain.entity.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Date

class PlayerViewModel(val track: Track, private val playerInteractor: PlayerInteractor) :
    ViewModel() {

    private val playerState = MutableLiveData<PlayerState>(PlayerState.Default)
    fun getPlayerState(): LiveData<PlayerState> = playerState

    private val progress = MutableLiveData(0)
    fun getProgress(): LiveData<Int> = progress

    private val isFavoriteState = MutableLiveData(false)
    fun getIsFavoriteState(): LiveData<Boolean> = isFavoriteState

    private var timerJob: Job? = null

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

}