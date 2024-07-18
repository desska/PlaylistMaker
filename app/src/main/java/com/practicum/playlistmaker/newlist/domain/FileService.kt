package com.practicum.playlistmaker.newlist.domain

interface FileService {
    fun saveImageToPrivateStorage(path: String): String

}