package com.practicum.playlistmaker.newlist.domain

import android.net.Uri

interface FileService {
    fun saveImageToPrivateStorage(uri: Uri): String

}