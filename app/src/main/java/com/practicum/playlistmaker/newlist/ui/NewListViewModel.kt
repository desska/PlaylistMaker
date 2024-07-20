package com.practicum.playlistmaker.newlist.ui

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.newlist.domain.NewListInteractor
import com.practicum.playlistmaker.newlist.domain.entity.NewListState
import com.practicum.playlistmaker.newlist.domain.entity.Playlist
import kotlinx.coroutines.launch

open class NewListViewModel(private val interactor: NewListInteractor) : ViewModel() {
    protected val listState = MutableLiveData<NewListState>(NewListState.NotReadyToSave)
    fun getListState(): LiveData<NewListState> = listState

    protected val coverState = MutableLiveData<Uri?>(null)
    fun getCoverState(): LiveData<Uri?> = coverState

    val descriptionState = MutableLiveData<String>()
    val nameState = MutableLiveData<String>()

    open fun onNameTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        listState.value = if (p0?.isEmpty() == true) {
            NewListState.NotReadyToSave
        } else {
            NewListState.ReadyToSave
        }
    }

    open fun onDescriptionTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    fun onBackPressed() {
        if (isUnsavedData()) {
            listState.value = NewListState.Confirm
        } else {
            listState.value = NewListState.Exit
        }
    }

    fun onConfirmPositive() {
        listState.value = NewListState.Exit
    }

    fun onConfirmNegative() {
        listState.value = if (nameState.value.isNullOrEmpty()) {
            NewListState.NotReadyToSave
        } else {
            NewListState.ReadyToSave
        }
    }

    open fun onCoverChanged(cover: Uri?) {
        coverState.value = cover
    }

    protected open fun getListToSave(): Playlist {
        val name = nameState.value ?: ""
        return Playlist(
            name = name,
            description = descriptionState.value ?: "",
            cover = if (coverState.value == null) {
                ""
            } else {
                coverState.value.toString()
            }
        )
    }

    fun saveList() {
        viewModelScope.launch {
            val playlist = getListToSave()
            interactor.addList(
                playlist
            )
            listState.value = NewListState.Created(playlist.name)
        }

    }

    private fun isUnsavedData(): Boolean {
        val coverIsEmpty = ((coverState.value == null) or (coverState.value == Uri.EMPTY))
        return !(nameState.value.isNullOrEmpty() and descriptionState.value.isNullOrEmpty() and coverIsEmpty)

    }

}

