package com.practicum.playlistmaker.editlist.ui

import android.net.Uri
import androidx.core.net.toUri
import com.practicum.playlistmaker.newlist.domain.NewListInteractor
import com.practicum.playlistmaker.newlist.domain.entity.NewListState
import com.practicum.playlistmaker.newlist.domain.entity.Playlist
import com.practicum.playlistmaker.newlist.ui.NewListViewModel

class EditListViewModel(private val playlist: Playlist, interactor: NewListInteractor) :
    NewListViewModel(interactor) {

    init {
        descriptionState.value = playlist.description
        nameState.value = playlist.name
        coverState.value = playlist.cover.toUri()

    }

    override fun onNameTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        val newList = Playlist(
            id = playlist.id,
            name = p0.toString(),
            description = descriptionState.value ?: "",
            cover = coverState.value.toString()
        )
        updateListState(newList)
    }

    override fun onDescriptionTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        val newList = Playlist(
            id = playlist.id,
            name = nameState.value ?: "",
            description = p0.toString(),
            cover = coverState.value.toString()
        )
        updateListState(newList)
    }

    private fun updateListState(newList: Playlist) {
        listState.value = if (isReadyToSave(newList)) {
            NewListState.ReadyToSave
        } else {
            NewListState.NotReadyToSave
        }
    }

    private fun isReadyToSave(newList: Playlist): Boolean {
        return (newList != playlist) and (newList.name.isNotEmpty())
    }

    override fun onCoverChanged(cover: Uri?) {
        super.onCoverChanged(cover)
        val newList = getListToSave()
        updateListState(newList)
    }

    override fun getListToSave(): Playlist {
        return Playlist(
            id = playlist.id,
            name = nameState.value ?: "",
            description = descriptionState.value ?: "",
            cover = coverState.value.toString()
        )
    }
}