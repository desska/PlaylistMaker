package com.practicum.playlistmaker.edittracks.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.edittracks.domain.EditTracksInteractor
import com.practicum.playlistmaker.edittracks.domain.entity.TracksState
import com.practicum.playlistmaker.newlist.domain.entity.Playlist
import com.practicum.playlistmaker.player.domain.entity.BottomSheetState
import com.practicum.playlistmaker.player.domain.entity.Track
import com.practicum.playlistmaker.playlist.domain.PlaylistInteractor
import com.practicum.playlistmaker.utils.SingleEventLiveData
import kotlinx.coroutines.launch

class EditTracksViewModel(
    private val playlistId: Int,
    private val interactor: EditTracksInteractor,
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {
    private val playlistState = MutableLiveData<Playlist>()
    fun observePlaylistState(): LiveData<Playlist> = playlistState

    private val tracksState = MutableLiveData<TracksState>()
    fun observeTracksState(): LiveData<TracksState> = tracksState

    private val tracksQuantityState = MutableLiveData<Int>()
    fun observeTracksQuantityState(): LiveData<Int> = tracksQuantityState

    private val tracksTimeState = MutableLiveData<Int>()
    fun observeTracksTimeState(): LiveData<Int> = tracksTimeState

    private val trackClickEvent = SingleEventLiveData<Track>()
    fun observeTrackClickEvent(): LiveData<Track> = trackClickEvent

    private val longTrackClickEvent = SingleEventLiveData<Pair<Playlist, Track>>()
    fun observeLongTrackClickEvent(): LiveData<Pair<Playlist, Track>> = longTrackClickEvent

    private val menuBottomSheetState = SingleEventLiveData<BottomSheetState>()
    fun observeMenuBottomSheetState(): LiveData<BottomSheetState> = menuBottomSheetState

    private val emptyShareToastEvent = SingleEventLiveData<Boolean>()
    fun observeEmptyShareToastEvent(): LiveData<Boolean> = emptyShareToastEvent

    private val editListEvent = SingleEventLiveData<Playlist>()
    fun observeEditListEvent(): LiveData<Playlist> = editListEvent

    private val deleteListDialogEvent = SingleEventLiveData<Boolean>()
    fun observeDeleteListDialogEvent(): LiveData<Boolean> = deleteListDialogEvent

    private val exitEvent = SingleEventLiveData<Boolean>()
    fun observeExitEvent(): LiveData<Boolean> = exitEvent

    fun share() {
        if (tracksState.value is TracksState.Content) {
            val text = getShareText()
            interactor.share(text)
        } else {
            emptyShareToastEvent.value = true
        }
    }

    private fun getShareText(): String {
        val playlist = playlistState.value ?: Playlist()
        val strings = mutableListOf<String>()
        strings.add(playlist.name)
        if (playlist.description.isNotEmpty()) {
            strings.add(playlist.description)
        }
        strings.add("${tracksQuantityState.value} треков")
        (tracksState.value as TracksState.Content).data.forEachIndexed { index, track ->
            strings.add("${index}. ${track.artistName} - ${track.trackName} (${track.trackTime})")
        }
        return strings.joinToString("\n")
    }

    fun onTrackClick(track: Track) {
        trackClickEvent.value = track
    }

    fun onLongTrackClick(track: Track): Boolean {
        if (track.trackId == null) {
            return false
        }

        longTrackClickEvent.value = Pair(playlistState.value ?: Playlist(), track)
        return false
    }

    fun onConfirmDeleteTrack() {
        val playlistId = longTrackClickEvent.value?.first?.id ?: return
        val trackId = longTrackClickEvent.value?.second?.trackId ?: return

        viewModelScope.launch {
            interactor.deleteTrack(playlistId = playlistId, trackId = trackId)
            fillData()
        }

    }

    fun onMenuClick() {
        menuBottomSheetState.value = BottomSheetState.Collapsed
    }

    fun onConfirmDeleteList() {
        viewModelScope.launch {
            interactor.deleteList(playlistId = playlistId)
        }
        exitEvent.value = true
    }

    fun fillData() {
        viewModelScope.launch {
            playlistInteractor.getList(playlistId).collect { playlist ->
                playlistState.postValue(playlist)

                interactor.getTracks(playlistId = playlist.id).collect {
                    tracksQuantityState.postValue(it.size)
                    if (it.isNotEmpty()) {
                        tracksState.postValue(TracksState.Content(it))
                        tracksTimeState.postValue(it.map { track -> track.trackTimeMillis ?: 0 }
                            .reduce { acc, i -> acc + i } / 60000)
                    } else {
                        tracksTimeState.postValue(0)
                        tracksState.postValue(TracksState.Empty)
                    }
                }
            }
        }
    }

    fun onEditPlaylistClick() {
        editListEvent.value = playlistState.value ?: Playlist()
    }

    fun onDeletePlaylistClick() {
        deleteListDialogEvent.value = true
    }

}