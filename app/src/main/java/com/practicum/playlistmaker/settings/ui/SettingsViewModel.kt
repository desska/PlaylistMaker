package com.practicum.playlistmaker.settings.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.sharing.domain.SharingInteractor

class SettingsViewModel(private val sharingInteractor: SharingInteractor, private val settingsInteractor: SettingsInteractor): ViewModel() {

    private var themeState = MutableLiveData(settingsInteractor.getIsDarkTheme())
    fun getTheme(): LiveData<Boolean> = themeState

    fun onToggleTheme(isDark: Boolean) {

        settingsInteractor.switchTheme(isDark)
        themeState.value = settingsInteractor.getIsDarkTheme()
    }

    fun onShare() = sharingInteractor.share()

    fun onSupport() = sharingInteractor.emailSupport()

    fun onOpenLink() = sharingInteractor.openLink()

}