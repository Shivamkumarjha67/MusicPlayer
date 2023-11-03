package com.example.musicplayer

import android.app.Activity
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class SearchResultRecyclerAdapter (val context : Activity, val dataList : List<Data>) :
    RecyclerView.Adapter<SearchResultRecyclerAdapter.MyViewHolder>() {

    class MyViewHolder(val itemView : View) : RecyclerView.ViewHolder(itemView) {
        val musicImage : ImageView
        val musicTitle : TextView
        val musicPlayButton : ImageButton
        val musicPauseButton : ImageButton
        val musicStopButton : ImageButton

        init {
            musicImage = itemView.findViewById(R.id.musicImage)
            musicTitle = itemView.findViewById(R.id.musicTitle)
            musicPlayButton = itemView.findViewById(R.id.btnMusicPlay)
            musicPauseButton = itemView.findViewById(R.id.btnMusicPause)
            musicStopButton = itemView.findViewById(R.id.btnMusicStop)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.search_result_card, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currData = dataList[position]
        holder.musicTitle.text = currData.title
        Picasso.get().load(currData.album.cover).into(holder.musicImage)

        var player = MediaPlayer.create(context, currData.preview.toUri())

        holder.musicPlayButton.setOnClickListener {
            player.start()

            holder.musicPlayButton.visibility = View.GONE
            holder.musicPauseButton.visibility = View.VISIBLE
            holder.musicStopButton.visibility = View.VISIBLE
        }

        holder.musicPauseButton.setOnClickListener {
            player.pause()

            holder.musicPlayButton.visibility = View.VISIBLE
            holder.musicPauseButton.visibility = View.GONE
            holder.musicStopButton.visibility = View.GONE
        }

        holder.musicStopButton.setOnClickListener {
            player.stop()

            holder.musicPlayButton.visibility = View.VISIBLE
            holder.musicPauseButton.visibility = View.GONE
            holder.musicStopButton.visibility = View.GONE

            player = MediaPlayer.create(context, currData.preview.toUri())
        }
    }
}