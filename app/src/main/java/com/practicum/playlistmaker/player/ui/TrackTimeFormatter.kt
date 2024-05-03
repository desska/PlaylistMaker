package com.practicum.playlistmaker.player.ui

import java.text.SimpleDateFormat
import java.util.Locale

object TrackTimeFormatter {

    fun formatTime(trackTimeMillis: Int?): String {

        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)
            .trimStart('0')

    }

}