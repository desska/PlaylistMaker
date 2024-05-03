package com.practicum.playlistmaker.player.ui

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.player.domain.PlayerInteractor
import com.practicum.playlistmaker.player.domain.entity.PlayerState
import com.practicum.playlistmaker.player.domain.entity.Track

class PlayerViewModel(track: Track, private val playerInteractor: PlayerInteractor) : ViewModel() {

    private val playerState = MutableLiveData<PlayerState>(PlayerState.Default)
    fun getPlayerState(): LiveData<PlayerState> = playerState

    private val progress = MutableLiveData(0)
    fun getProgress(): LiveData<Int> = progress

    private var handler: Handler? = null

    private val updater = object : Runnable {

        override fun run() {

            progress.value = playerInteractor.getCurrentPosition()
            handler?.postDelayed(this, REFRESH_TIME_MS)

        }

    }

    init {

        handler = Handler(Looper.getMainLooper())

        val url = track.previewUrl ?: ""

        if (url == "") {

            playerState.postValue(PlayerState.Error("url error"))


        } else {

            playerInteractor.prepare(url, object : PlayerInteractor.OnPreparedListener {

                override fun onPrepared() {

                    playerState.postValue(PlayerState.Paused)

                }

                override fun onComplete() {

                    playerState.postValue(PlayerState.Prepared)

                }

            })

        }


    }

    override fun onCleared() {
        super.onCleared()

        if (playerState.value !is PlayerState.Error) {

            handler?.removeCallbacks(updater)

        }


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
        handler?.postDelayed(

            updater,
            REFRESH_TIME_MS

        )

        playerState.postValue(PlayerState.Playing)

    }

    private fun pause() {

        if (playerState.value is PlayerState.Error) return
        playerInteractor.pause()
        handler?.removeCallbacks(updater)
        playerState.postValue(PlayerState.Paused)

    }

    private fun release() {

        if (playerState.value is PlayerState.Error) return
        playerInteractor.release()
        playerState.postValue(PlayerState.Paused)

    }

    companion object {

        private const val REFRESH_TIME_MS = 300L

        fun getViewModelFactory(track: Track, interactor: PlayerInteractor): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {

                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return PlayerViewModel(
                        track, interactor
                    ) as T

                }

            }
    }


}