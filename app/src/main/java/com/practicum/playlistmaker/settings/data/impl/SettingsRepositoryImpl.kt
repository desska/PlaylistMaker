package com.practicum.playlistmaker.settings.data.impl

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.settings.domain.SettingsRepository

class SettingsRepositoryImpl(val context: Context): SettingsRepository {

    override fun getIsDarkTheme(): Boolean  {

            val sharedPrefs = context.getSharedPreferences(
                COMMON_PREFERENCE,
                Application.MODE_PRIVATE
            )
            return sharedPrefs.getBoolean(DARK_THEME_MODE, false)

    }

    override fun switchTheme(isDarkTheme: Boolean) {

        val sharedPrefs = context.getSharedPreferences(
            COMMON_PREFERENCE,
            Application.MODE_PRIVATE
        )
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

        const val COMMON_PREFERENCE = "COMMON_PREFERENCE"
        const val DARK_THEME_MODE = "dark_theme_mode"

    }

}