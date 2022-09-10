package com.kube.musicplayer.helper

import java.time.Duration
import java.util.concurrent.TimeUnit

class DateHelper {
    constructor()


    fun formatDuration(duration: Long): String{
        val minutes=TimeUnit.MINUTES.convert(duration,TimeUnit.MILLISECONDS)
        val seconds=(TimeUnit.SECONDS.convert(duration,TimeUnit.MILLISECONDS)-minutes*TimeUnit.SECONDS.convert(1,TimeUnit.MINUTES))
        return String.format("%02d:%02d",minutes,seconds)
    }

}


