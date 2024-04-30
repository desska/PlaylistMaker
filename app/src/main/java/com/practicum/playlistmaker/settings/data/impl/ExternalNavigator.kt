package com.practicum.playlistmaker.settings.data.impl

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri

class ExternalNavigator(val context: Context) {

    fun share(text: String) {

        val intent = Intent().apply {

            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }

        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {

        }
    }

    fun openUrl(url: String) {

        val intent = Intent().apply {

            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, url)
            type = "text/plain"
        }

        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {

        }

    }

    fun sendEmail(address: String, subj: String, text: String) {

        val intent = Intent().apply {

            action = Intent.ACTION_SENDTO
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(address))
            putExtra(Intent.EXTRA_SUBJECT, subj)
            putExtra(Intent.EXTRA_TEXT, text)
        }

        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {

        }

    }

}