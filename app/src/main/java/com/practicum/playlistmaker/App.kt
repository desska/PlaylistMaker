package com.practicum.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

const val COMMON_PREFERENCE = "COMMON_PREFERENCE"
const val DARK_THEME_MODE = "dark_theme_mode"

class App : Application() {

    var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        val sharedPrefs = getSharedPreferences(COMMON_PREFERENCE, MODE_PRIVATE)

        darkTheme = sharedPrefs.getBoolean(DARK_THEME_MODE, false)

        switchTheme(darkTheme)

    }

    fun switchTheme(darkThemeEnabled: Boolean) {

        darkTheme = darkThemeEnabled

        val sharedPrefs = getSharedPreferences(COMMON_PREFERENCE, MODE_PRIVATE)
        sharedPrefs.edit()
            .putBoolean(DARK_THEME_MODE, darkTheme)
            .apply()

        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }

        )
    }

}