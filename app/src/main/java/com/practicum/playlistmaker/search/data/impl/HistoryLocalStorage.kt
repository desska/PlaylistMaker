package com.practicum.playlistmaker.search.data.impl

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.player.domain.entity.Track
import java.lang.reflect.Type

class HistoryLocalStorage(
    private val sharedPreference: SharedPreferences,
) {

    fun add(track: Track) {

        val list: MutableList<Track> = get()
        val index = list.indexOfFirst { it.trackId == track.trackId }

        if (index != -1) {
            list.removeAt(index)
        }

        list.add(0, track)

        if (list.size > MAX_TRACKS) {

            list.subList(MAX_TRACKS, list.size - 1 + 1).clear()

        }

        put(list)

    }

    fun clear() {
        put(listOf())
    }

    fun get(): MutableList<Track> {

        val listAsJson = sharedPreference.getString(RECENT_TRACKS_KEY, null)
        val list = if (listAsJson != null) {
            val type: Type = object : TypeToken<MutableList<Track>>() {}.type
            Gson().fromJson(listAsJson, type)

        } else {

            mutableListOf<Track>()

        }

        return list

    }

    private fun put(list: List<Track>) {

        sharedPreference.edit()
            .putString(RECENT_TRACKS_KEY, Gson().toJson(list))
            .apply()

    }

    companion object {

        const val MAX_TRACKS = 10
        const val RECENT_TRACKS_KEY = "RECENT_TRACKS"

    }

}