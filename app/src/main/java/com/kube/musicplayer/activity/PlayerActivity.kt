package com.kube.musicplayer.activity

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.media.MediaPlayer
import android.media.audiofx.AudioEffect
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kube.musicplayer.MainActivity
import com.kube.musicplayer.R
import com.kube.musicplayer.databinding.ActivityPlayerBinding
import com.kube.musicplayer.helper.DateHelper
import com.kube.musicplayer.model.Song
import com.kube.musicplayer.model.exitApplication
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
        var min15: Boolean = false
        var min30: Boolean = false
        var min60: Boolean = false

        @SuppressLint("StaticFieldLeak")
        lateinit var binding: ActivityPlayerBinding
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeLayout()
        getIntents()
        buttonActions()

    }

    private fun buttonActions() {
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
        binding.equalizerBtn.setOnClickListener {
            try {
                val eqIntent = Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL)
                eqIntent.putExtra(
                    AudioEffect.EXTRA_AUDIO_SESSION,
                    songService!!.mediaPlayer!!.audioSessionId
                )
                eqIntent.putExtra(AudioEffect.EXTRA_PACKAGE_NAME, baseContext.packageName)
                eqIntent.putExtra(AudioEffect.EXTRA_CONTENT_TYPE, AudioEffect.CONTENT_TYPE_MUSIC)
                startActivityForResult(eqIntent, 13)
            } catch (e: Exception) {
                Toast.makeText(this, "Equalizer Feature not Supported", Toast.LENGTH_SHORT).show()
            }
        }
        binding.timerBtn.setOnClickListener {
            val timer = min15 || min30 || min60
            if (!timer)
                showBottomSheetDialog()
            else {
                val builder = MaterialAlertDialogBuilder(this)
                builder.setTitle("Stop Timer").setMessage("Do you want to stop timer")
                    .setPositiveButton("Yes") { _, _ ->
                        min15 = false
                        min30 = false
                        min60 = false
                        binding.timerBtn.setColorFilter(ContextCompat.getColor(this, R.color.pink))
                    }
                    .setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
                val customDialog = builder.create()
                customDialog.show()
                customDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED)
                customDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED)

            }
        }
        binding.shareBtn.setOnClickListener {
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.type = "audio/*"
            shareIntent.putExtra(
                Intent.EXTRA_STREAM,
                Uri.parse(songListPlayerActivity[songPosition].path)
            )
            startActivity(Intent.createChooser(shareIntent, "Sharing Music File!!"))
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
            "NowPlayingFragment" -> {
                setLayout()
                binding.songDurationStartTv.text=DateHelper().formatDuration(songService!!.mediaPlayer!!.currentPosition.toLong())
                binding.songDurationEndTv.text=DateHelper().formatDuration(songService!!.mediaPlayer!!.duration.toLong())
                binding.songSb.progress=songService!!.mediaPlayer!!.currentPosition
                binding.songSb.max= songService!!.mediaPlayer!!.duration
            }
            "SongAdapterSearch" -> {
                startingService()
                songListPlayerActivity = ArrayList()
                songListPlayerActivity.addAll(MainActivity.songListSearch)
                setLayout()
            }
            "SongAdapter" -> {
                startingService()
                songListPlayerActivity = ArrayList()
                songListPlayerActivity.addAll(MainActivity.songListMainActivity)
                setLayout()
            }
            "MainActivity" -> {
                startingService()
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
        if (repeat)
            binding.repeatBtn.setColorFilter(ContextCompat.getColor(this, R.color.purple_500))
        if (min15 || min60 || min30)
            binding.timerBtn.setColorFilter(ContextCompat.getColor(this, R.color.purple_500))
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 13 || resultCode == RESULT_OK) {
            return
        }
    }

    private fun showBottomSheetDialog() {
        val dialog = BottomSheetDialog(this@PlayerActivity)
        dialog.setContentView(R.layout.bottom_sheet_dialog)
        dialog.show()
        dialog.findViewById<LinearLayout>(R.id.min15_ll)?.setOnClickListener {

            Toast.makeText(baseContext, "Music will stop after 15 minutes", Toast.LENGTH_SHORT)
                .show()
            binding.timerBtn.setColorFilter(ContextCompat.getColor(this, R.color.purple_500))
            min15 = true
            Thread {
                Thread.sleep(15 * 60000)
                if (min15) exitApplication()
            }.start()
            dialog.dismiss()
        }
        dialog.findViewById<LinearLayout>(R.id.min30_ll)?.setOnClickListener {

            Toast.makeText(baseContext, "Music will stop after 30 minutes", Toast.LENGTH_SHORT)
                .show()

            binding.timerBtn.setColorFilter(ContextCompat.getColor(this, R.color.purple_500))
            min30 = true
            Thread {
                Thread.sleep(30 * 60000)
                if (min30) exitApplication()
            }.start()
            dialog.dismiss()
        }
        dialog.findViewById<LinearLayout>(R.id.min60_ll)?.setOnClickListener {
            Toast.makeText(baseContext, "Music will stop after 60 minutes", Toast.LENGTH_SHORT)
                .show()
            binding.timerBtn.setColorFilter(ContextCompat.getColor(this, R.color.purple_500))
            min60 = true
            Thread {
                Thread.sleep(60 * 60000)
                if (min60) exitApplication()
            }.start()
            dialog.dismiss()
        }
    }
}