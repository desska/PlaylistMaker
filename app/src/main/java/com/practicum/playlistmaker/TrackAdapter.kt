package com.practicum.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter(val data: MutableList<Track>, private val searchHistory: SearchHistory? = null) : RecyclerView.Adapter<TrackViewHolder>() {

     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return TrackViewHolder(view)

    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {

        holder.bind(data[position])

        if (searchHistory != null) {

            holder.itemView.setOnClickListener {
                searchHistory.addTrack(data[position])
            }

        }

    }

    fun add(index: Int, item: Track) {

        data.add(index, item)
        notifyItemInserted(index)

    }

    fun removeAt(index: Int) = data.removeAt(index)

    fun clear() {

        data.clear()
        notifyDataSetChanged()

    }

    fun removeRange(from: Int, to: Int) {

        data.subList(from, to + 1).clear()
        notifyItemRangeRemoved(from, to - from + 1)

    }

    fun isEmpty(): Boolean = data.size == 0

}