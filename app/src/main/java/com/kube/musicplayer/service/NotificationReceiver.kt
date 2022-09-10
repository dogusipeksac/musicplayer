package com.kube.musicplayer.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.kube.musicplayer.PlayerActivity
import com.kube.musicplayer.R
import kotlin.system.exitProcess

class NotificationReceiver:BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when(intent?.action){
            ApplicationClass.PREVIOUS -> Toast.makeText(context,"previous clicked",Toast.LENGTH_SHORT).show()
            ApplicationClass.NEXT -> Toast.makeText(context,"next clicked",Toast.LENGTH_SHORT).show()
            ApplicationClass.PLAY -> {
                if(PlayerActivity.isPlaying) pauseSong() else playSong()
            }
            ApplicationClass.EXIT -> {
                PlayerActivity.songService!!.stopForeground(true)
                PlayerActivity.songService=null
                exitProcess(1)
            }
        }
    }
    private fun playSong(){
        PlayerActivity.isPlaying=true
        PlayerActivity.songService!!.mediaPlayer!!.start()
        PlayerActivity.songService!!.showNotification(R.drawable.pause_icon)
        PlayerActivity.binding.playPauseBtn.setIconResource(R.drawable.pause_icon)
    }
    private fun pauseSong(){
        PlayerActivity.isPlaying=false
        PlayerActivity.songService!!.mediaPlayer!!.pause()
        PlayerActivity.songService!!.showNotification(R.drawable.play_icon)
        PlayerActivity.binding.playPauseBtn.setIconResource(R.drawable.play_icon)
    }
}