package com.practicum.playlistmaker.newlist.data.impl

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri
import com.practicum.playlistmaker.newlist.domain.FileService
import java.io.File
import java.io.FileOutputStream

class FileServiceImpl(private val context: Context) : FileService {
    override fun saveImageToPrivateStorage(path: String): String {
        val uri = path.toUri()
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), DIRECTORY)
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File.createTempFile(FILE_PREFIX, FILE_SUFFIX, filePath)
        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory.decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        return file.path
    }

    companion object {
        const val DIRECTORY = "pl_maker"
        const val FILE_PREFIX = "cover_"
        const val FILE_SUFFIX = ""
    }
}