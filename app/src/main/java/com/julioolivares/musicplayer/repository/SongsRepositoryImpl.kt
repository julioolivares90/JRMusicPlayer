package com.julioolivares.musicplayer.repository

import android.content.Context
import android.provider.MediaStore
import android.util.Log
import com.julioolivares.musicplayer.helper.Constants
import com.julioolivares.musicplayer.helper.Constants.checkDuration
import com.julioolivares.musicplayer.helper.Constants.toast
import com.julioolivares.musicplayer.models.Song

class SongsRepositoryImpl (private val context: Context)  : SongsRepository {
     var songs : MutableList<Song> = ArrayList()

    override fun getAllSongs(): MutableList<Song> {
        val allSongURI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val selection = MediaStore.Audio.Media.IS_MUSIC + "!=0"

        val sortOrder = "${MediaStore.Audio.Media.DISPLAY_NAME} ASC"

        val cursor = context.applicationContext.contentResolver.query(
            allSongURI,null,selection,null,sortOrder
        )

        when {
            cursor == null -> {
                return  mutableListOf()
            }
            !cursor.moveToFirst() -> {
               return mutableListOf()
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

                    songs.add(song)

                }while (cursor.moveToNext())

            }

        }
        cursor.close()

        return  songs
    }
}