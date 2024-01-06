package com.practicum.playlistmaker

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.settings_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = null

        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val shareButton = findViewById<ImageView>(R.id.share_button)
        shareButton.setOnClickListener{

            val intent = Intent().apply {

                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, getString(R.string.app_url))
                type = "text/plain"
            }

            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {

            }

        }

        val supportButton = findViewById<ImageView>(R.id.support_button)
        supportButton.setOnClickListener {

            val intent = Intent().apply {

                action = Intent.ACTION_SENDTO
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.supp_email_address)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.supp_email_subj))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.supp_email_text))
            }

            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {

            }

        }

        val offerButton = findViewById<ImageView>(R.id.offer_button)
        offerButton.setOnClickListener {

            val intent = Intent().apply {

                action = Intent.ACTION_VIEW
                data = Uri.parse(getString(R.string.offer_url))
            }

            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {

            }

        }

    }
}