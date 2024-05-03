package com.practicum.playlistmaker.settings.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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

    companion object {

        fun getViewModelFactory(sharingInteractor: SharingInteractor, settingsInteractor: SettingsInteractor): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {

                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SettingsViewModel(
                        sharingInteractor, settingsInteractor
                    ) as T

                }

            }
    }
}