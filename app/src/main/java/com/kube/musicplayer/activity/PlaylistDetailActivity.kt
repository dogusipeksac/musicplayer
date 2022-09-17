package com.kube.musicplayer.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kube.musicplayer.MainActivity
import com.kube.musicplayer.R
import com.kube.musicplayer.adapter.SongAdapter
import com.kube.musicplayer.databinding.ActivityPlaylistDetailBinding

class PlaylistDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityPlaylistDetailBinding
    lateinit var adapter: SongAdapter

    companion object {
        var currentPlaylistPosition: Int = -1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.coolPink)
        binding = ActivityPlaylistDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        currentPlaylistPosition = intent.extras?.get("index") as Int
        binding.playlistDetailRv.setItemViewCacheSize(10)
        binding.playlistDetailRv.setHasFixedSize(true)
        binding.playlistDetailRv.layoutManager = LinearLayoutManager(this)
        PlaylistActivity.songPlaylist.ref[currentPlaylistPosition].playlist.addAll(MainActivity.songListMainActivity)
        PlaylistActivity.songPlaylist.ref[currentPlaylistPosition].playlist.shuffle()
        adapter = SongAdapter(
            this,
            PlaylistActivity.songPlaylist.ref[currentPlaylistPosition].playlist,
            playlistDetails = true
        )
        binding.playlistDetailRv.adapter = adapter
        binding.backBtn.setOnClickListener {
            finish()
        }
        binding.shuffleBtn.setOnClickListener {
            val intent = Intent(this, PlayerActivity::class.java)
            intent.putExtra("index", 0)
            intent.putExtra("class", "PlaylistDetailsShuffle")
            startActivity(intent)
        }
        binding.addBtn.setOnClickListener {
            startActivity(Intent(this,SelectionActivity::class.java))
        }

    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()
        binding.playlistNameTv.text =
            PlaylistActivity.songPlaylist.ref[currentPlaylistPosition].name
        binding.infoTv.text = "Total ${adapter.itemCount} Songs.\n" +
                "Created On: ${PlaylistActivity.songPlaylist.ref[currentPlaylistPosition].createdON}\n" +
                "-- ${PlaylistActivity.songPlaylist.ref[currentPlaylistPosition].createdBy}"
        if (adapter.itemCount > 0) {
            Glide.with(this@PlaylistDetailActivity)
                .load(PlaylistActivity.songPlaylist.ref[currentPlaylistPosition].playlist[0].artUri)
                .apply(RequestOptions().placeholder(R.drawable.music_icon).centerCrop())
                .into(binding.playlistImage)
            binding.shuffleBtn.visibility = View.VISIBLE
        }

    }
}