package com.practicum.playlistmaker.settings.data.impl

import android.content.Context
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.settings.data.SettingsRepository

class SettingsRepositoryImpl(val context: Context): SettingsRepository {

    override fun getIsDarkTheme() = (context as App).getIsDarkTheme()

    override fun switchTheme(isDarkTheme: Boolean) = (context as App).switchTheme(isDarkTheme)

}