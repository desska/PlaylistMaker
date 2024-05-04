package com.practicum.playlistmaker.settings.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.settingsToolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = null

        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val themeSwitcher = binding.themeSwitcher

        viewModel.getTheme().observe(this) {


            themeSwitcher.isChecked = it

        }

        themeSwitcher.setOnCheckedChangeListener { _, checked ->

            viewModel.onToggleTheme(checked)

        }

        val shareButton = binding.shareButton
        shareButton.setOnClickListener {

            viewModel.onShare()

        }

        val supportButton = binding.supportButton
        supportButton.setOnClickListener {

            viewModel.onSupport()


        }

        val offerButton = binding.offerButton
        offerButton.setOnClickListener {

            viewModel.onOpenLink()

        }

    }
}