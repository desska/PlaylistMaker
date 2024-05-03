package com.practicum.playlistmaker.settings.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

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

        val sharingInteractor = Creator.provideSharingInteractor(this)
        val settingsInteractor = Creator.ProvideSettingsInteractor(applicationContext)

        val viewModel = ViewModelProvider(
            this,
            SettingsViewModel.getViewModelFactory(sharingInteractor, settingsInteractor)
        )[SettingsViewModel::class.java]

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