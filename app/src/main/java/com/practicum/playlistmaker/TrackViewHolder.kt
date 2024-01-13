package com.practicum.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val coverView = itemView.findViewById<ImageView>(R.id.track_item_cover)
    private val trackNameView = itemView.findViewById<TextView>(R.id.track_item_track_name)
    private val artistNameView = itemView.findViewById<TextView>(R.id.track_item_artist_name)
    private val trackTimeView = itemView.findViewById<TextView>(R.id.track_item_track_time)
    fun bind(item: Track) {

        trackNameView.text = item.trackName
        artistNameView.text = item.artistName
        trackTimeView.text = item.trackTime

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