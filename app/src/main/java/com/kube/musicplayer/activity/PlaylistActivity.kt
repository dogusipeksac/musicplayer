package com.kube.musicplayer.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kube.musicplayer.R
import com.kube.musicplayer.databinding.ActivityPlaylistBinding

class PlaylistActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlaylistBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.coolPink)
        binding=ActivityPlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.backBtn.setOnClickListener {
            finish()
        }
    }
}