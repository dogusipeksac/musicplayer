package com.kube.musicplayer.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kube.musicplayer.R
import com.kube.musicplayer.activity.PlayerActivity
import com.kube.musicplayer.activity.PlaylistActivity
import com.kube.musicplayer.activity.PlaylistDetailActivity
import com.kube.musicplayer.databinding.ItemFavoriteBinding
import com.kube.musicplayer.databinding.ItemPlaylistBinding
import com.kube.musicplayer.model.Playlist
import com.kube.musicplayer.model.Song
import com.kube.musicplayer.model.exitApplication

class PlaylistAdapter(private val context: Context, private var playlistList: ArrayList<Playlist>) :
    RecyclerView.Adapter<PlaylistAdapter.Holder>() {

    class Holder(binding: ItemPlaylistBinding) : RecyclerView.ViewHolder(binding.root) {
        val image = binding.playlistImg
        val name = binding.playlistNameTv
        val root = binding.root
        val delete = binding.playlistDeleteBtn

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(ItemPlaylistBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.name.text = playlistList[position].name
        holder.name.isSelected = true
        holder.delete.setOnClickListener {
            val builder = MaterialAlertDialogBuilder(context)
            builder.setTitle(playlistList[position].name)
                .setMessage("Do you want to delete playlist?")
                .setPositiveButton("Yes") { dialog, _ ->
                    PlaylistActivity.songPlaylist.ref.removeAt(position)
                    refreshPlaylist()
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
            val customDialog = builder.create()
            customDialog.show()
            customDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED)
            customDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED)

        }
        holder.root.setOnClickListener {
            val intent=Intent(context,PlaylistDetailActivity::class.java)
            intent.putExtra("index",position)
            ContextCompat.startActivity(context,intent,null)
        }
        if (PlaylistActivity.songPlaylist.ref[position].playlist.size>0){
            Glide.with(context)
                .load(PlaylistActivity.songPlaylist.ref[position].playlist[0].artUri)
                .apply(RequestOptions().placeholder(R.drawable.music_icon).centerCrop())
                .into(holder.image)
        }
    }

    override fun getItemCount(): Int {
        return playlistList.size
    }

    fun refreshPlaylist() {
        playlistList = ArrayList()
        playlistList.addAll(PlaylistActivity.songPlaylist.ref)
        notifyDataSetChanged()
    }
}