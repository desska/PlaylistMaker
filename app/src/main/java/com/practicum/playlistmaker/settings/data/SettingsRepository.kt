package com.practicum.playlistmaker.settings.data

interface SettingsRepository {

    fun getIsDarkTheme(): Boolean

    fun switchTheme(isDarkTheme: Boolean)


}