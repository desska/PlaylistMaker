package com.practicum.playlistmaker.media.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.player.domain.entity.Track

class FavoriteAdapter(
    private val clickListener: (Track) -> Unit
) : RecyclerView.Adapter<FavoriteViewHolder>() {

    private var data: MutableList<Track> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return FavoriteViewHolder(view)

    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(data[position])

        holder.itemView.setOnClickListener {
            clickListener(data[position])
        }

    }

    fun setItems(trackList: List<Track>) {
        data.clear()
        data.addAll(trackList)
        notifyDataSetChanged()

    }

    fun clear() {
        data.clear()
        notifyDataSetChanged()
    }

}