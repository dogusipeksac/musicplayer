package com.kube.musicplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kube.musicplayer.databinding.ActivityFavoriteBinding

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding:ActivityFavoriteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_MusicPlayer)
        binding=ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}