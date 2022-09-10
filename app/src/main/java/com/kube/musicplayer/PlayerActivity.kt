package com.kube.musicplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kube.musicplayer.databinding.ActivityPlayerBinding
import com.kube.musicplayer.databinding.ActivityPlaylistBinding

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.coolPink)
        binding=ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}