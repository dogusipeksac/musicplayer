package com.kube.musicplayer.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kube.musicplayer.R
import com.kube.musicplayer.activity.PlayerActivity
import com.kube.musicplayer.databinding.ItemFavoriteBinding
import com.kube.musicplayer.model.Song

class FavouriteAdapter(private val context: Context, private var songList: ArrayList<Song>) :
    RecyclerView.Adapter<FavouriteAdapter.Holder>() {

    class Holder(binding: ItemFavoriteBinding) : RecyclerView.ViewHolder(binding.root) {
        val image=binding.songIm
        val name = binding.songNameTv
        val root=binding.root

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(ItemFavoriteBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        Glide.with(context)
            .load(songList[position].artUri)
            .apply(RequestOptions().placeholder(R.drawable.music_icon).centerCrop())
            .into(holder.image)
        holder.name.text=songList[position].title
        holder.root.setOnClickListener{
            val intent = Intent(context, PlayerActivity::class.java)
            intent.putExtra("index", position)
            intent.putExtra("class", "FavouriteAdapter")
            ContextCompat.startActivity(context, intent, null)
        }

    }

    override fun getItemCount(): Int {
        return songList.size
    }




}