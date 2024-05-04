package com.practicum.playlistmaker.settings.data.impl

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.settings.domain.SettingsRepository

class SettingsRepositoryImpl(private val sharedPrefs: SharedPreferences) :
    SettingsRepository {

    override fun getIsDarkTheme(): Boolean {

        return sharedPrefs.getBoolean(DARK_THEME_MODE, false)

    }

    override fun switchTheme(isDarkTheme: Boolean) {

        sharedPrefs.edit()
            .putBoolean(DARK_THEME_MODE, isDarkTheme)
            .apply()

        AppCompatDelegate.setDefaultNightMode(
            if (isDarkTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }

        )

    }

    companion object {

        const val DARK_THEME_MODE = "dark_theme_mode"

    }

}