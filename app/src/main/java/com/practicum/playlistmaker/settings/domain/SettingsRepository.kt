package com.practicum.playlistmaker.settings.domain

interface SettingsRepository {

    fun getIsDarkTheme(): Boolean

    fun switchTheme(isDarkTheme: Boolean)


}