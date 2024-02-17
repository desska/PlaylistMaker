package com.practicum.playlistmaker

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson

class TrackAdapter(
    val data: MutableList<Track>,
    private val context: Context,
    private val searchHistory: SearchHistory? = null
) :
    RecyclerView.Adapter<TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return TrackViewHolder(view)

    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {

        holder.bind(data[position])


        holder.itemView.setOnClickListener {
            val track = data[position]
            searchHistory?.addTrack(track)
            val intent = Intent(context, PlayerActivity::class.java)
            intent.putExtra(PLAYER_TRACKS_KEY, Gson().toJson(track))
            context.startActivity(intent)

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