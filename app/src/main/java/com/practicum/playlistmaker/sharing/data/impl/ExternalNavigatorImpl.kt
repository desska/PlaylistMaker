package com.practicum.playlistmaker.sharing.data.impl

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.settings.domain.entity.EmailData
import com.practicum.playlistmaker.sharing.domain.ExternalNavigator

class ExternalNavigatorImpl(val context: Context): ExternalNavigator {

    override fun share(text: String) {

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

    override fun openUrl(url: String) {

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

    override fun sendEmail(address: String, subj: String, text: String) {

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

    override fun getShareUrl(): String = context.getString(R.string.app_url)

    override fun getSupportData(): EmailData = EmailData(
        context.getString(R.string.supp_email_address),
        context.getString(R.string.supp_email_subj),
        context.getString(R.string.supp_email_text)
    )

    override fun getOpenUrl(): String = context.getString(R.string.app_url)

}