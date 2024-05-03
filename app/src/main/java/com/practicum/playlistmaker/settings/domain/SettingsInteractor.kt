package com.practicum.playlistmaker.settings.domain

interface SettingsInteractor {

    fun getIsDarkTheme(): Boolean

    fun switchTheme(isDarkTheme: Boolean)

}