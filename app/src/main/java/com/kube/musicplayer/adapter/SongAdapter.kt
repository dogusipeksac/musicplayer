package com.kube.musicplayer.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kube.musicplayer.MainActivity
import com.kube.musicplayer.PlayerActivity
import com.kube.musicplayer.R
import com.kube.musicplayer.databinding.ActivityFavoriteBinding
import com.kube.musicplayer.databinding.ItemSongBinding
import com.kube.musicplayer.helper.DateHelper
import com.kube.musicplayer.model.Song

class SongAdapter(private val context: Context, private var songList: ArrayList<Song>) :
    RecyclerView.Adapter<SongAdapter.Holder>() {

    class Holder(binding: ItemSongBinding) : RecyclerView.ViewHolder(binding.root) {
        val title = binding.songNameTv
        val album = binding.songAlbumTv
        val duration = binding.songDurationTv
        val image = binding.songIv
        val root = binding.root
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongAdapter.Holder {
        return Holder(ItemSongBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: SongAdapter.Holder, position: Int) {
        holder.title.text = songList[position].title
        holder.album.text = songList[position].album
        holder.duration.text = DateHelper().formatDuration(songList[position].duration)
        Glide.with(context)
            .load(songList[position].artUri)
            .apply(RequestOptions().placeholder(R.drawable.music_icon).centerCrop())
            .into(holder.image)

        holder.root.setOnClickListener {
            when {
                MainActivity.search -> sendIntent(ref = "SongAdapterSearch", position = position)
                else -> sendIntent(ref = "SongAdapter", position = position)
            }


        }
    }

    override fun getItemCount(): Int {
        return songList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateSongList(searchList: ArrayList<Song>) {
        songList = ArrayList()
        songList.addAll(searchList)
        notifyDataSetChanged()
    }

    private fun sendIntent(ref: String, position: Int) {
        val intent = Intent(context, PlayerActivity::class.java)
        intent.putExtra("index", position)
        intent.putExtra("class", ref)
        ContextCompat.startActivity(context, intent, null)
    }
}