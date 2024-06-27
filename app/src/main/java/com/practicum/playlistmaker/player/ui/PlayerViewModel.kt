package com.practicum.playlistmaker.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.player.domain.PlayerInteractor
import com.practicum.playlistmaker.player.domain.entity.PlayerState
import com.practicum.playlistmaker.player.domain.entity.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(track: Track, private val playerInteractor: PlayerInteractor) : ViewModel() {

    private val playerState = MutableLiveData<PlayerState>(PlayerState.Default)
    fun getPlayerState(): LiveData<PlayerState> = playerState

    private val progress = MutableLiveData(0)
    fun getProgress(): LiveData<Int> = progress

    private var timerJob: Job? = null


    init {
        val url = track.previewUrl ?: ""

        if (url == "") {
            playerState.postValue(PlayerState.Error("url error"))

        } else {
            playerInteractor.prepare(url, object : PlayerInteractor.OnPreparedListener {
                override fun onPrepared() {
                    playerState.value = PlayerState.Paused
                }

                override fun onComplete() {
                    playerState.value = PlayerState.Paused
                    timerJob?.cancel()
                    progress.value = 0
                }

            })

        }

    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (playerState.value is PlayerState.Playing) {
                delay(REFRESH_TIME_MS)
                progress.value = playerInteractor.getCurrentPosition()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()

        release()
    }

    fun onPlaybackControl() {
        when (playerState.value) {
            is PlayerState.Playing -> {
                pause()
            }

            is PlayerState.Paused -> {
                start()
            }

            else -> {}
        }
    }

    fun onPause(): Unit = pause()

    private fun start() {
        if (playerState.value is PlayerState.Error) return
        playerInteractor.start()
        playerState.value = PlayerState.Playing
        startTimer()
    }

    private fun pause() {
        if (playerState.value is PlayerState.Error) return
        playerInteractor.pause()
        playerState.value = PlayerState.Paused
        timerJob?.cancel()
    }

    private fun release() {
        if (playerState.value is PlayerState.Error) return
        playerInteractor.release()
        playerState.value = PlayerState.Paused

    }

    companion object {

        private const val REFRESH_TIME_MS = 300L

    }

}