package com.practicum.playlistmaker.edittracks.ui

import com.practicum.playlistmaker.newlist.domain.entity.Playlist
import com.practicum.playlistmaker.player.domain.entity.Track
import com.practicum.playlistmaker.search.ui.TrackAdapter
import com.practicum.playlistmaker.search.ui.TrackViewHolder

class EditTracksTrackAdapter(
    private val clickListener: (Track) -> Unit,
    private val longClickListener: (Track) -> Boolean,
) : TrackAdapter(clickListener) {
    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(data[position])

        holder.itemView.setOnClickListener {
            clickListener(data[position])
        }

        holder.itemView.setOnLongClickListener {
            longClickListener(data[position])
        }
    }

}