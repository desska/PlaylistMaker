package com.practicum.playlistmaker.newlist.data.impl

import androidx.core.net.toUri
import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.newlist.data.converters.PlayListDbConverter
import com.practicum.playlistmaker.newlist.domain.FileService
import com.practicum.playlistmaker.newlist.domain.NewListRepository
import com.practicum.playlistmaker.newlist.domain.entity.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NewListRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val fileService: FileService,
    private val converter: PlayListDbConverter
) : NewListRepository {
    override suspend fun addList(list: Playlist) {
        withContext(Dispatchers.IO) {
            val path = if (list.cover.isNotEmpty()) {
                fileService.saveImageToPrivateStorage(list.cover.toUri())
            } else {
                ""
            }
            val entity = converter.map(list, path)
            appDatabase.PlaylistDao().addList(entity)
        }

    }
}