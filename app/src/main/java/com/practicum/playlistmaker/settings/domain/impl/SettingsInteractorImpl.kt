package com.practicum.playlistmaker.settings.domain.impl

import com.practicum.playlistmaker.settings.domain.SettingsRepository
import com.practicum.playlistmaker.settings.domain.SettingsInteractor

class SettingsInteractorImpl(val repository: SettingsRepository): SettingsInteractor {

    override fun getIsDarkTheme(): Boolean {

        return repository.getIsDarkTheme()

    }

    override fun switchTheme(isDarkTheme: Boolean) {

        repository.switchTheme(isDarkTheme)

    }


}