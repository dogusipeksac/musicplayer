package com.kube.musicplayer.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kube.musicplayer.R
import com.kube.musicplayer.activity.PlayerActivity
import com.kube.musicplayer.databinding.FragmentNowPlayingBinding
import com.kube.musicplayer.model.setSongPosition


class NowPlayingFragment : Fragment() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var binding: FragmentNowPlayingBinding
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_now_playing, container, false);
        binding = FragmentNowPlayingBinding.bind(view)
        binding.root.visibility = View.INVISIBLE
        binding.playPauseBtn.setOnClickListener {
            if (PlayerActivity.isPlaying) pauseSong() else playSong()
        }
        binding.nextBtn.setOnClickListener {
           previousOrNextSong(true)
        }
        binding.root.setOnClickListener{
            val intent = Intent(requireContext(), PlayerActivity::class.java)
            intent.putExtra("index", PlayerActivity.songPosition)
            intent.putExtra("class", "NowPlayingFragment")
            ContextCompat.startActivity(requireContext(), intent, null)
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        if (PlayerActivity.songService != null) {
            binding.root.visibility = View.VISIBLE
            binding.songNameTv.isSelected=true
            Glide.with(this)
                .load(PlayerActivity.songListPlayerActivity[PlayerActivity.songPosition].artUri)
                .apply(RequestOptions().placeholder(R.drawable.music_icon).centerCrop())
                .into(binding.songIm)
            binding.songNameTv.text = PlayerActivity.songListPlayerActivity[PlayerActivity.songPosition].title
            if (PlayerActivity.isPlaying) binding.playPauseBtn.setIconResource(R.drawable.pause_icon)
            else binding.playPauseBtn.setIconResource(R.drawable.play_icon)
        }
    }

    private fun playSong() {
        PlayerActivity.songService!!.mediaPlayer!!.start()
        binding.playPauseBtn.setIconResource(R.drawable.pause_icon)
        PlayerActivity.songService!!.showNotification(R.drawable.pause_icon)
        PlayerActivity.binding.nextBtn.setIconResource(R.drawable.pause_icon)
        PlayerActivity.isPlaying=true
    }

    private fun pauseSong() {
        PlayerActivity.songService!!.mediaPlayer!!.pause()
        binding.playPauseBtn.setIconResource(R.drawable.play_icon)
        PlayerActivity.songService!!.showNotification(R.drawable.play_icon)
        PlayerActivity.binding.nextBtn.setIconResource(R.drawable.play_icon)
        PlayerActivity.isPlaying=false
    }

    private fun  previousOrNextSong(increment:Boolean){
        setSongPosition(increment)
        PlayerActivity.songService!!.createMediaPlayer()
        PlayerActivity.binding.songTv.text =
            PlayerActivity.songListPlayerActivity[PlayerActivity.songPosition].title
        Glide.with(this)
            .load(PlayerActivity.songListPlayerActivity[PlayerActivity.songPosition].artUri)
            .apply(RequestOptions().placeholder(R.drawable.music_icon).centerCrop())
            .into(binding.songIm)
        binding.songNameTv.text = PlayerActivity.songListPlayerActivity[PlayerActivity.songPosition].title
        PlayerActivity.songService!!.showNotification(R.drawable.pause_icon)
        playSong()
    }
}