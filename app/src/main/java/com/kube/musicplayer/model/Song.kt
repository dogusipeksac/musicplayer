package com.kube.musicplayer.model

import com.kube.musicplayer.activity.FavouriteActivity
import com.kube.musicplayer.activity.PlayerActivity
import kotlin.system.exitProcess

data class Song(
    val id: String,
    val title: String,
    val album: String,
    val artist: String,
    val duration: Long = 0,
    val path: String,
    val artUri: String
)

fun setSongPosition(increment: Boolean) {
    if (!PlayerActivity.repeat) {
        if (increment) {
            if (PlayerActivity.songListPlayerActivity.size - 1 == PlayerActivity.songPosition)
                PlayerActivity.songPosition = 0
            else ++PlayerActivity.songPosition
        } else {
            if (0 == PlayerActivity.songPosition)
                PlayerActivity.songPosition = PlayerActivity.songListPlayerActivity.size - 1
            else --PlayerActivity.songPosition
        }
    }

}
fun exitApplication(){
    if (PlayerActivity.songService != null) {
        PlayerActivity.songService!!.stopForeground(true)
        PlayerActivity.songService!!.mediaPlayer!!.release()
        PlayerActivity.songService = null
        exitProcess(1)
    }
}
fun favoriteChecker(id: String):Int{
    PlayerActivity.isFavorite=false
    FavouriteActivity.favoriteSongs.forEachIndexed { index, song ->
        if(id==song.id){
            PlayerActivity.isFavorite=true
            return index
        }
    }
    return -1
}