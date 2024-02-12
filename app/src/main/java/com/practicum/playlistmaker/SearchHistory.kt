package com.practicum.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

const val RECENT_TRACKS_KEY = "RECENT_TRACKS"
const val MAX_TRACKS = 10

class SearchHistory(
    private val sharedPreference: SharedPreferences,
    private val adapter: TrackAdapter
) {

    fun addTrack(track: Track) {

        val index = adapter.data.indexOfFirst { it.trackId == track.trackId }
        if (index != -1) {
            adapter.removeAt(index)
        }

        adapter.add(0, track)

        if (adapter.data.size > MAX_TRACKS) {

            adapter.removeRange(MAX_TRACKS, adapter.data.size - 1)

        }

        saveToPreference(adapter.data)

    }

    fun clear() {

        adapter.clear()
        saveToPreference(adapter.data)
    }

    private fun saveToPreference(list: List<Track>) {

        sharedPreference.edit()
            .putString(RECENT_TRACKS_KEY, Gson().toJson(list))
            .apply()

    }


    companion object {

        fun getListFromShared(sharedPreference: SharedPreferences): List<Track> {

            val listAsJson = sharedPreference.getString(RECENT_TRACKS_KEY, null)
            val list = if (listAsJson != null) {
                val type: Type = object : TypeToken<List<Track>>() {}.type
                Gson().fromJson(listAsJson, type)

            } else {

                listOf<Track>()

            }

            return list

        }


    }

}