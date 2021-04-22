package com.julioolivares.musicplayer.viewModels

import android.app.Application
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.julioolivares.musicplayer.helper.Constants
import com.julioolivares.musicplayer.helper.Constants.toast
import com.julioolivares.musicplayer.models.Song

class ListMusicViewModel (private val context: Context) : ViewModel() {
    private  var _songs : MutableLiveData<List<Song>> = MutableLiveData()

    private var songsList : MutableList<Song> = ArrayList()

    fun sons() : LiveData<List<Song>> = _songs

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAllSons() {
        val allSongURI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val selection = MediaStore.Audio.Media.IS_MUSIC + "!=0"

        val sortOrder = "${MediaStore.Audio.Media.DISPLAY_NAME} ASC"

        val cursor = context.applicationContext.contentResolver.query(
                allSongURI,null,selection,null,sortOrder
        )

        when {
            cursor == null -> {
                context.toast("Ocurrio un error")
            }
            !cursor.moveToFirst() -> {
                context.toast("No se encontraron archivos en el dispositivo")
            }
            else -> {
                val titleColumn : Int = cursor.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE)
                val idColumn : Int = cursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID)
                do {
                    val thisId = cursor.getLong(idColumn)
                    val thisTitle = cursor.getString(titleColumn)

                    Log.d("ID ->",thisId.toString())
                    Log.d("TITLE ->",thisTitle)

                    val songUri = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                    val songAuthor = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                    val songTitle = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME))
                    val songDuration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))

                    //converting then song duration
                    var songDurlong  :  Long

                    songDurlong = checkDuration(songDuration)

                    songDuration?.let {
                        songDurlong = it.toLong()
                    }
                    //songDuration.toLong()

                    val song = Song(songTitle = songTitle,
                            songArtist = songAuthor,
                            songUri = songUri,
                            songDuration = Constants.durationConverter(songDurlong),
                            idSong = thisId
                    )
                    songsList.add(song)

                }while (cursor.moveToNext())

                _songs.value = songsList
            }

        }
        cursor?.close()
    }

    private fun checkDuration(songDuration: String?) : Long{
        return songDuration?.toLong() ?: 0L
    }
}