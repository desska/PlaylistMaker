package com.practicum.playlistmaker.sharing.domain.impl

import com.practicum.playlistmaker.sharing.domain.ExternalNavigator
import com.practicum.playlistmaker.sharing.domain.SharingInteractor

class SharingInteractorImpl(private val navigator: ExternalNavigator) :
    SharingInteractor {

    override fun share() = navigator.share(navigator.getShareUrl())

    override fun emailSupport() {

        val data = navigator.getSupportData()
        navigator.sendEmail(data.address, data.subj, data.text)

    }

    override fun openLink() = navigator.openUrl(navigator.getOpenUrl())

}