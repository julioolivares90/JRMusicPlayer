package com.julioolivares.musicplayer.helper

import android.content.Context
import android.widget.Toast
import java.util.concurrent.TimeUnit

object Constants {
    const val REQUEST_CODE_FOR_PERMISSIONS = 101

    const  val ACTION_PLAY : String = "com.example.action.PLAY"

    fun Context.toast(msg : String ){
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show()
    }

    fun durationConverter(duration : Long) : String{
        return String.format("%02d:%02d",
        TimeUnit.MILLISECONDS.toMillis(duration),
                TimeUnit.MICROSECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(duration)
                )
        )
    }

     fun checkDuration(songDuration: String?) : Long{
        return songDuration?.toLong() ?: 0L
    }
}