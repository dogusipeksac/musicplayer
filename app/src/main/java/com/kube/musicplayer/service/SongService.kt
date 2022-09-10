package com.kube.musicplayer.service

import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import androidx.media.app.NotificationCompat
import com.kube.musicplayer.PlayerActivity
import com.kube.musicplayer.R

class SongService : Service() {

    private var myBinder=MyBinder()
    var mediaPlayer:MediaPlayer?=null
    private lateinit var mediaSession:MediaSessionCompat

    override fun onBind(intent: Intent?): IBinder {
        mediaSession= MediaSessionCompat(baseContext,"My Song")
        return myBinder
    }

    inner class MyBinder : Binder() {
        fun currentService(): SongService {
            return this@SongService
        }
    }

    fun showNotification(){
        val notification=androidx.core.app.NotificationCompat
            .Builder(baseContext,ApplicationClass.CHANNEL_ID)
            .setContentTitle(PlayerActivity.songListPlayerActivity[PlayerActivity.songPosition].title)
            .setContentText(PlayerActivity.songListPlayerActivity[PlayerActivity.songPosition].artist)
            .setSmallIcon(R.drawable.note_icon)
            .setLargeIcon(BitmapFactory.decodeResource(resources,R.drawable.music_icon))
            .setStyle(NotificationCompat.MediaStyle().setMediaSession(mediaSession.sessionToken))
            .setPriority(androidx.core.app.NotificationCompat.PRIORITY_HIGH)
            .setVisibility(androidx.core.app.NotificationCompat.VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true)
            .addAction(R.drawable.before_icon,"Previous",null)
            .addAction(R.drawable.play_icon,"Play",null)
            .addAction(R.drawable.next_icon,"Next",null)
            .addAction(R.drawable.exit_to_app_icon,"Exit",null)
            .build()
        startForeground(13,notification)
    }
}