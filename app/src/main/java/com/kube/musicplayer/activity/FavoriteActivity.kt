package com.kube.musicplayer.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.kube.musicplayer.MainActivity
import com.kube.musicplayer.R
import com.kube.musicplayer.adapter.FavoriteAdapter
import com.kube.musicplayer.adapter.SongAdapter
import com.kube.musicplayer.databinding.ActivityFavoriteBinding
import com.kube.musicplayer.model.Song

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding:ActivityFavoriteBinding
    private lateinit var favoriteAdapter: FavoriteAdapter

    companion object{
        var favoriteSongs:ArrayList<Song>  = ArrayList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.coolPink)
        binding=ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.favoriteRv.setHasFixedSize(true)
        binding.favoriteRv.setItemViewCacheSize(13)
        binding.favoriteRv.layoutManager = GridLayoutManager(this@FavoriteActivity,4 )
        favoriteAdapter = FavoriteAdapter(this@FavoriteActivity, favoriteSongs)
        binding.favoriteRv.adapter = favoriteAdapter
    }

}