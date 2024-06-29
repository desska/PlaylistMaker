package com.practicum.playlistmaker.search.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.utils.Utils
import com.practicum.playlistmaker.player.domain.entity.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val coverView = itemView.findViewById<ImageView>(R.id.track_item_cover)
    private val trackNameView = itemView.findViewById<TextView>(R.id.track_item_track_name)
    private val artistNameView = itemView.findViewById<TextView>(R.id.track_item_artist_name)
    private val trackTimeView = itemView.findViewById<TextView>(R.id.track_item_track_time)
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    fun bind(item: Track) {

        trackNameView.text = item.trackName
        artistNameView.text = item.artistName
        trackTimeView.text = dateFormat.format(item.trackTimeMillis)

        Glide.with(itemView)
            .load(item.artworkUrl100)
            .centerCrop()
            .transform(
                RoundedCorners(
                    Utils.dpToPx(
                        itemView.context.resources.getInteger(R.integer.track_cover_corner)
                            .toFloat(), itemView.context
                    )
                )
            )
            .placeholder(R.drawable.placeholder)
            .into(coverView)
    }

}