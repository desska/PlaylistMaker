package com.practicum.playlistmaker.playlist.ui

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.PlaylistItemBinding
import com.practicum.playlistmaker.newlist.domain.entity.Playlist

class PlaylistViewHolder(private val binding: PlaylistItemBinding, private val context: Context) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Playlist) {
        binding.name.text = item.name

        binding.qty.text = context.resources.getQuantityString(R.plurals.tracks, item.quantity, item.quantity)

        if (item.cover.toUri() == Uri.EMPTY) {
            binding.cover.setImageResource(R.drawable.placeholder_medium)
            binding.cover.scaleType = ImageView.ScaleType.CENTER_INSIDE
        } else {
            binding.cover.setImageURI(item.cover.toUri())
            binding.cover.scaleType = ImageView.ScaleType.FIT_XY
        }

    }
}