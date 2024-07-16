package com.practicum.playlistmaker.player.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.databinding.PlaylistListItemBinding
import com.practicum.playlistmaker.newlist.domain.entity.Playlist

class PlaylistAdapter(
    private val context: Context,
    private val onClickAction: (Playlist, Int) -> Unit,
    private val trackId: Int?
) :
    RecyclerView.Adapter<PlaylistViewHolder>() {
    private val data: MutableList<Playlist> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val binding =
            PlaylistListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaylistViewHolder(binding, context)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(data[position])
        if (trackId != null) {
            holder.itemView.setOnClickListener {
                onClickAction(data[position], trackId)
            }
        }

    }

    fun setItems(trackList: List<Playlist>) {
        data.clear()
        data.addAll(trackList)
        notifyDataSetChanged()

    }

    fun clear() {
        if (isEmpty()) return

        data.clear()
        notifyDataSetChanged()

    }

    private fun isEmpty(): Boolean = data.size == 0

}