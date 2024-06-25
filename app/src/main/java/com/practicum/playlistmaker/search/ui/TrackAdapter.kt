package com.practicum.playlistmaker.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.player.domain.entity.Track

class TrackAdapter(
    private val clickListener: (Track) -> Unit
) :
    RecyclerView.Adapter<TrackViewHolder>() {

    private var data: MutableList<Track> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return TrackViewHolder(view)

    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
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
        if (isEmpty()) return

        data.clear()
        notifyDataSetChanged()

    }

    fun isEmpty(): Boolean = data.size == 0

}