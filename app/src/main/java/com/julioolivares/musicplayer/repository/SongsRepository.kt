package com.julioolivares.musicplayer.repository

import com.julioolivares.musicplayer.models.Song

interface SongsRepository {
    fun getAllSongs() : MutableList<Song>
}