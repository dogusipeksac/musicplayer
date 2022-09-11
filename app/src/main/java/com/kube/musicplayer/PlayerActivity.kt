package com.kube.musicplayer

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kube.musicplayer.databinding.ActivityPlayerBinding
import com.kube.musicplayer.databinding.ActivityPlaylistBinding
import com.kube.musicplayer.helper.DateHelper
import com.kube.musicplayer.model.Song
import com.kube.musicplayer.model.setSongPosition
import com.kube.musicplayer.service.SongService
import java.lang.Exception

class PlayerActivity : AppCompatActivity(), ServiceConnection, MediaPlayer.OnCompletionListener {

    companion object {
        lateinit var songListPlayerActivity: ArrayList<Song>
        var songPosition: Int = 0
        var isPlaying: Boolean = false
        var songService: SongService? = null
        var repeat: Boolean = false

        @SuppressLint("StaticFieldLeak")
        lateinit var binding: ActivityPlayerBinding
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startingService()
        initializeLayout()
        getIntents()

        binding.backBtn.setOnClickListener {
            finish()
        }
        binding.playPauseBtn.setOnClickListener {
            if (isPlaying) pauseSong() else playSong()
        }
        binding.previousBtn.setOnClickListener {
            previousOrNextSong(false)
        }
        binding.nextBtn.setOnClickListener {
            previousOrNextSong(true)
        }
        binding.songSb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekbar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) songService!!.mediaPlayer!!.seekTo(progress)
            }

            override fun onStartTrackingTouch(seekbar: SeekBar?) = Unit
            override fun onStopTrackingTouch(seekbar: SeekBar?) = Unit
        })
        binding.repeatBtn.setOnClickListener {
            if (!repeat) {
                repeat = true
                binding.repeatBtn.setColorFilter(ContextCompat.getColor(this, R.color.purple_500))
            } else {
                repeat = false
                binding.repeatBtn.setColorFilter(ContextCompat.getColor(this, R.color.pink))
            }
        }
    }

    private fun startingService() {
        val intent = Intent(this, SongService::class.java)
        bindService(intent, this, BIND_AUTO_CREATE)
        startService(intent)
    }

    private fun initializeLayout() {
        setTheme(R.style.coolPink)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun getIntents() {
        songPosition = intent.getIntExtra("index", 0)
        when (intent.getStringExtra("class")) {
            "SongAdapter" -> {
                songListPlayerActivity = ArrayList()
                songListPlayerActivity.addAll(MainActivity.songListMainActivity)
                setLayout()


            }
            "MainActivity" -> {
                songListPlayerActivity = ArrayList()
                songListPlayerActivity.addAll(MainActivity.songListMainActivity)
                songListPlayerActivity.shuffle()
                setLayout()

            }
        }
    }

    private fun setLayout() {
        Glide.with(this)
            .load(songListPlayerActivity[songPosition].artUri)
            .apply(RequestOptions().placeholder(R.drawable.music_icon).centerCrop())
            .into(binding.songIv)
        binding.songTv.text = songListPlayerActivity[songPosition].title
        if (repeat) binding.repeatBtn.setColorFilter(
            ContextCompat.getColor(
                this,
                R.color.purple_500
            )
        )
    }

    private fun createMediaPlayer() {
        try {
            if (songService!!.mediaPlayer == null) songService!!.mediaPlayer = MediaPlayer()
            songService!!.mediaPlayer!!.reset()
            songService!!.mediaPlayer!!.setDataSource(songListPlayerActivity[songPosition].path)
            songService!!.mediaPlayer!!.prepare()
            songService!!.mediaPlayer!!.start()
            isPlaying = true
            binding.playPauseBtn.setIconResource(R.drawable.pause_icon)
            songService!!.showNotification(R.drawable.pause_icon)
            binding.songDurationStartTv.text =
                DateHelper().formatDuration(songService!!.mediaPlayer!!.currentPosition.toLong())
            binding.songDurationEndTv.text =
                DateHelper().formatDuration(songService!!.mediaPlayer!!.duration.toLong())
            binding.songSb.progress = 0
            binding.songSb.max = songService!!.mediaPlayer!!.duration
            songService!!.mediaPlayer!!.setOnCompletionListener(this)
        } catch (e: Exception) {
            return
        }
    }

    private fun playSong() {
        binding.playPauseBtn.setIconResource(R.drawable.pause_icon)
        songService!!.showNotification(R.drawable.pause_icon)
        isPlaying = true
        songService!!.mediaPlayer!!.start()
    }

    private fun pauseSong() {
        binding.playPauseBtn.setIconResource(R.drawable.play_icon)
        songService!!.showNotification(R.drawable.play_icon)
        isPlaying = false
        songService!!.mediaPlayer!!.pause()
    }

    private fun previousOrNextSong(increment: Boolean) {
        setSongPosition(increment)
        setLayout()
        createMediaPlayer()

    }


    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder = service as SongService.MyBinder
        songService = binder.currentService()
        createMediaPlayer()
        songService!!.seekBarSetup()

    }

    override fun onServiceDisconnected(name: ComponentName?) {
        songService = null
    }

    override fun onCompletion(mp: MediaPlayer?) {
        setSongPosition(true)
        createMediaPlayer()
        try {
            setLayout()
        } catch (e: Exception) {
            return
        }

    }
}