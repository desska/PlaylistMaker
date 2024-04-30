package com.practicum.playlistmaker.sharing.domain.impl

import android.content.Context
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.settings.data.impl.ExternalNavigator
import com.practicum.playlistmaker.sharing.domain.SharingInteractor
import com.practicum.playlistmaker.settings.domain.entity.EmailData

class SharingInteractorImpl(val context: Context, val navigator: ExternalNavigator) :
    SharingInteractor {

    override fun share() = navigator.share(getShareUrl())

    override fun emailSupport() {

        val data = getSupportData()
        navigator.sendEmail(data.address, data.subj, data.text)

    }

    override fun openLink() = navigator.openUrl(getOpenUrl())

    private fun getShareUrl(): String = context.getString(R.string.app_url)

    private fun getSupportData(): EmailData = EmailData(
        context.getString(R.string.supp_email_address),
        context.getString(R.string.supp_email_subj),
        context.getString(R.string.supp_email_text)
    )

    private fun getOpenUrl(): String = context.getString(R.string.app_url)

}