package com.kube.musicplayer.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder

class SongService : Service() {

    private var myBinder=MyBinder()
    var mediaPlayer:MediaPlayer?=null

    override fun onBind(intent: Intent?): IBinder {
        return myBinder
    }

    inner class MyBinder : Binder() {
        fun currentService(): SongService {
            return this@SongService
        }
    }
}