
package com.practicum.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchButton =  findViewById<Button>(R.id.search_button)
        searchButton.setOnClickListener {
            val settingsIntent = Intent(this, SearchActivity::class.java)
            startActivity(settingsIntent)
        }

        val settingsButton =  findViewById<Button>(R.id.settings_button)
        settingsButton.setOnClickListener {
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }

        val libraryButton =  findViewById<Button>(R.id.library_button)
        libraryButton.setOnClickListener {
            val settingsIntent = Intent(this, LibraryActivity::class.java)
            startActivity(settingsIntent)
        }

    }

    override fun onStop() {
        super.onStop()

        val sharedPrefs = getSharedPreferences(COMMON_PREFERENCE, MODE_PRIVATE)
        sharedPrefs.edit()
            .putBoolean(DARK_THEME_MODE, (applicationContext as App).darkTheme)
            .apply()

    }
}