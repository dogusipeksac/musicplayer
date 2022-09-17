package com.kube.musicplayer.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kube.musicplayer.activity.PlayerActivity
import com.kube.musicplayer.R
import com.kube.musicplayer.fragment.NowPlayingFragment
import com.kube.musicplayer.model.exitApplication
import com.kube.musicplayer.model.favoriteChecker
import com.kube.musicplayer.model.setSongPosition

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            ApplicationClass.PREVIOUS -> previousOrNextSong(false, context!!)
            ApplicationClass.NEXT -> previousOrNextSong(true, context!!)
            ApplicationClass.PLAY -> {
                if (PlayerActivity.isPlaying) pauseSong() else playSong()
            }
            ApplicationClass.EXIT -> { exitApplication() }
        }
    }

    private fun playSong() {
        PlayerActivity.isPlaying = true
        PlayerActivity.songService!!.mediaPlayer!!.start()
        PlayerActivity.songService!!.showNotification(R.drawable.pause_icon)
        PlayerActivity.binding.playPauseBtn.setIconResource(R.drawable.pause_icon)
        NowPlayingFragment.binding.playPauseBtn.setIconResource(R.drawable.pause_icon)
    }

    private fun pauseSong() {
        PlayerActivity.isPlaying = false
        PlayerActivity.songService!!.mediaPlayer!!.pause()
        PlayerActivity.songService!!.showNotification(R.drawable.play_icon)
        PlayerActivity.binding.playPauseBtn.setIconResource(R.drawable.play_icon)
        NowPlayingFragment.binding.playPauseBtn.setIconResource(R.drawable.play_icon)
    }

    private fun previousOrNextSong(increment: Boolean, context: Context) {
        setSongPosition(increment)
        PlayerActivity.songService!!.createMediaPlayer()
        Glide.with(context)
            .load(PlayerActivity.songListPlayerActivity[PlayerActivity.songPosition].artUri)
            .apply(RequestOptions().placeholder(R.drawable.music_icon).centerCrop())
            .into(PlayerActivity.binding.songIv)
        PlayerActivity.binding.songTv.text = PlayerActivity.songListPlayerActivity[PlayerActivity.songPosition].title
        Glide.with(context)
            .load(PlayerActivity.songListPlayerActivity[PlayerActivity.songPosition].artUri)
            .apply(RequestOptions().placeholder(R.drawable.music_icon).centerCrop())
            .into(NowPlayingFragment.binding.songIm)
        NowPlayingFragment.binding.songNameTv.text = PlayerActivity.songListPlayerActivity[PlayerActivity.songPosition].title

        playSong()
        PlayerActivity.fIndex= favoriteChecker(PlayerActivity.songListPlayerActivity[PlayerActivity.songPosition].id)
        if (PlayerActivity.isFavorite) PlayerActivity.binding.favoriteBtn.setImageResource(R.drawable.favorite_icon)
        else PlayerActivity.binding.favoriteBtn.setImageResource(R.drawable.favorite_empty)
    }
}