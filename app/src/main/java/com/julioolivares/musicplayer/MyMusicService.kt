package com.julioolivares.musicplayer

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.os.PowerManager
import com.julioolivares.musicplayer.helper.Constants.ACTION_PLAY

class MyMusicService : Service() ,MediaPlayer.OnPreparedListener {
    private var mediaPlayer : MediaPlayer? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        when(intent?.action){
            ACTION_PLAY -> {
                mediaPlayer = MediaPlayer()
                mediaPlayer?.apply {
                    setOnPreparedListener(this@MyMusicService)
                    prepareAsync()
                    setWakeMode(applicationContext,PowerManager.PARTIAL_WAKE_LOCK)
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }


    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null

    }

    override fun onPrepared(mp: MediaPlayer?) {
        mediaPlayer?.start()
    }


}