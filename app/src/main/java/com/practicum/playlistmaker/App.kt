package com.practicum.playlistmaker

import android.app.Application
import com.practicum.playlistmaker.creator.Creator

class App : Application() {

    var isDarkTheme = false

    override fun onCreate() {
        super.onCreate()

        val interactor = Creator.ProvideSettingsInteractor(this)

        isDarkTheme = interactor.getIsDarkTheme()
        interactor.switchTheme(isDarkTheme)

    }

}