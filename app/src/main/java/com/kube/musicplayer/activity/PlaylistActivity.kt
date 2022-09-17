package com.kube.musicplayer.activity

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kube.musicplayer.MainActivity
import com.kube.musicplayer.R
import com.kube.musicplayer.adapter.PlaylistAdapter
import com.kube.musicplayer.adapter.SongAdapter
import com.kube.musicplayer.databinding.ActivityPlaylistBinding
import com.kube.musicplayer.databinding.DialogAddPlaylistBinding
import com.kube.musicplayer.model.Playlist
import com.kube.musicplayer.model.SongPlaylist
import com.kube.musicplayer.model.exitApplication
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PlaylistActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlaylistBinding
    private lateinit var adapter: PlaylistAdapter


    companion object {
        var songPlaylist: SongPlaylist = SongPlaylist()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.coolPink)
        binding = ActivityPlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.playlistRv.setHasFixedSize(true)
        binding.playlistRv.setItemViewCacheSize(13)
        binding.playlistRv.layoutManager = GridLayoutManager(this@PlaylistActivity, 2)
        adapter = PlaylistAdapter(this@PlaylistActivity, playlistList = songPlaylist.ref)
        binding.playlistRv.adapter = adapter


        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.addBtn.setOnClickListener {
            customAlertDialog()
        }
    }

    private fun customAlertDialog() {
        val customDialog = LayoutInflater.from(this@PlaylistActivity)
            .inflate(R.layout.dialog_add_playlist, binding.root, false)
        val binder = DialogAddPlaylistBinding.bind(customDialog)

        val builder = MaterialAlertDialogBuilder(this)
        builder.setView(customDialog).setTitle("Playlist Details")
            .setPositiveButton("ADD") { dialog, _ ->
                val playlistName = binder.playlistNameTv.text
                val createdBy = binder.userNameTv.text
                if (playlistName != null && createdBy != null) {
                    if (playlistName.isNotEmpty() && createdBy.isNotEmpty()) {
                        addPlaylist(playlistName.toString(), createdBy.toString())
                    }
                }

                dialog.dismiss()
            }.show()
    }

    private fun addPlaylist(name: String, createdBy: String) {
        var playlistExist = false
        for (i in songPlaylist.ref) {
            if (name == i.name) {
                playlistExist = true
                break
            }
        }
        if (playlistExist) {
            Toast.makeText(this, "Playlist Exist!!", Toast.LENGTH_SHORT).show()
        } else {
            val tempPlaylist = Playlist()
            tempPlaylist.name = name
            tempPlaylist.playlist = ArrayList()
            tempPlaylist.createdBy = createdBy
            val calender = Calendar.getInstance().time
            val sdf = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
            tempPlaylist.createdON = sdf.format(calender)
            songPlaylist.ref.add(tempPlaylist)
            adapter.refreshPlaylist()
        }
    }
}