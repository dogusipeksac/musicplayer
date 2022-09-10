package com.kube.musicplayer.helper

import android.media.MediaMetadataRetriever

class ImageHelper {
    fun getImageByteArray(path:String): ByteArray? {
        val retriever=MediaMetadataRetriever()
        retriever.setDataSource(path)
        return retriever.embeddedPicture

    }
}