package com.practicum.playlistmaker

import android.app.Application
import com.practicum.playlistmaker.di.dataModule
import com.practicum.playlistmaker.di.interactorModule
import com.practicum.playlistmaker.di.repositoryModule
import com.practicum.playlistmaker.di.viewModuleModule
import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    private var isDarkTheme = false

    override fun onCreate() {
        super.onCreate()

        startKoin {

            androidContext(this@App)
            modules(dataModule, repositoryModule, interactorModule, viewModuleModule)

        }

        val interactor: SettingsInteractor = getKoin().get()

        isDarkTheme = interactor.getIsDarkTheme()
        interactor.switchTheme(isDarkTheme)

    }

}