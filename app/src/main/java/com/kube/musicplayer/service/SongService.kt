package com.kube.musicplayer.service

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.support.v4.media.session.MediaSessionCompat
import androidx.media.app.NotificationCompat
import com.kube.musicplayer.PlayerActivity
import com.kube.musicplayer.R
import com.kube.musicplayer.helper.DateHelper
import com.kube.musicplayer.helper.ImageHelper
import java.lang.Exception

class SongService : Service() {

    private var myBinder = MyBinder()
    var mediaPlayer: MediaPlayer? = null
    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var runnable: Runnable

    override fun onBind(intent: Intent?): IBinder {
        mediaSession = MediaSessionCompat(baseContext, "My Song")
        return myBinder
    }

    inner class MyBinder : Binder() {
        fun currentService(): SongService {
            return this@SongService
        }
    }

    fun showNotification(playPauseBtn: Int) {

        val prevIntent = Intent(
            baseContext,
            NotificationReceiver::class.java
        ).setAction(ApplicationClass.PREVIOUS)
        val prevPendingIntent = PendingIntent.getBroadcast(
            baseContext,
            0,
            prevIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val playIntent =
            Intent(baseContext, NotificationReceiver::class.java).setAction(ApplicationClass.PLAY)
        val playPendingIntent = PendingIntent.getBroadcast(
            baseContext,
            0,
            playIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val nextIntent =
            Intent(baseContext, NotificationReceiver::class.java).setAction(ApplicationClass.NEXT)
        val nextPendingIntent = PendingIntent.getBroadcast(
            baseContext,
            0,
            nextIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val exitIntent =
            Intent(baseContext, NotificationReceiver::class.java).setAction(ApplicationClass.EXIT)
        val exitPendingIntent = PendingIntent.getBroadcast(
            baseContext,
            0,
            exitIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val imageArt =
            ImageHelper().getImageByteArray(PlayerActivity.songListPlayerActivity[PlayerActivity.songPosition].path)
        val image = if (imageArt != null) {
            BitmapFactory.decodeByteArray(imageArt, 0, imageArt.size)
        } else {
            BitmapFactory.decodeResource(resources, R.drawable.music_icon)
        }
        val notification = androidx.core.app.NotificationCompat
            .Builder(baseContext, ApplicationClass.CHANNEL_ID)
            .setContentTitle(PlayerActivity.songListPlayerActivity[PlayerActivity.songPosition].title)
            .setContentText(PlayerActivity.songListPlayerActivity[PlayerActivity.songPosition].artist)
            .setSmallIcon(R.drawable.note_icon)
            .setLargeIcon(image)
            .setStyle(NotificationCompat.MediaStyle().setMediaSession(mediaSession.sessionToken))
            .setPriority(androidx.core.app.NotificationCompat.PRIORITY_HIGH)
            .setVisibility(androidx.core.app.NotificationCompat.VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true)
            .addAction(R.drawable.before_icon, "Previous", prevPendingIntent)
            .addAction(playPauseBtn, "Play", playPendingIntent)
            .addAction(R.drawable.next_icon, "Next", nextPendingIntent)
            .addAction(R.drawable.exit_to_app_icon, "Exit", exitPendingIntent)
            .build()
        startForeground(13, notification)
    }

     fun createMediaPlayer() {
        try {
            if (PlayerActivity.songService!!.mediaPlayer == null) PlayerActivity.songService!!.mediaPlayer = MediaPlayer()
            PlayerActivity.songService!!.mediaPlayer!!.reset()
            PlayerActivity.songService!!.mediaPlayer!!.setDataSource(PlayerActivity.songListPlayerActivity[PlayerActivity.songPosition].path)
            PlayerActivity.songService!!.mediaPlayer!!.prepare()
            PlayerActivity.songService!!.mediaPlayer!!.start()
            PlayerActivity.isPlaying = true
            PlayerActivity.binding.playPauseBtn.setIconResource(R.drawable.pause_icon)
            PlayerActivity.songService!!.showNotification(R.drawable.pause_icon)
            PlayerActivity.binding.songDurationStartTv.text= DateHelper().formatDuration(mediaPlayer!!.currentPosition.toLong())
            PlayerActivity.binding.songDurationEndTv.text= DateHelper().formatDuration(mediaPlayer!!.duration.toLong())
            PlayerActivity.binding.songSb.progress=0
            PlayerActivity.binding.songSb.max= mediaPlayer!!.duration

        } catch (e: Exception) {
            return
        }
    }
    fun seekBarSetup(){
        runnable = Runnable {
            PlayerActivity.binding.songDurationStartTv.text= DateHelper().formatDuration(mediaPlayer!!.currentPosition.toLong())
            PlayerActivity.binding.songSb.progress=mediaPlayer!!.currentPosition
            Handler(Looper.getMainLooper()).postDelayed(runnable,200)
        }
        Handler(Looper.getMainLooper()).postDelayed(runnable,0)
    }
}