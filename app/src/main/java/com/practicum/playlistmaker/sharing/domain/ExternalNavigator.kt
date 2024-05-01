package com.practicum.playlistmaker.sharing.domain

import com.practicum.playlistmaker.settings.domain.entity.EmailData

interface ExternalNavigator {

    fun share(text: String)

    fun openUrl(url: String)

    fun sendEmail(address: String, subj: String, text: String)

    fun getShareUrl(): String

    fun getSupportData(): EmailData

    fun getOpenUrl(): String

}