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
import com.julioolivares.musicplayer.repository.SongsRepository

class ListMusicViewModel (private val repository: SongsRepository) : ViewModel() {
    private  var _songs : MutableLiveData<List<Song>> = MutableLiveData()

    //private var songsList : MutableList<Song> = ArrayList()

    private var _error : MutableLiveData<Boolean> = MutableLiveData()

    fun error() : LiveData<Boolean> = _error

    fun sons() : LiveData<List<Song>> = _songs

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAllSons() {
      val songs = repository.getAllSongs()

        if (songs.isNotEmpty()){
            _songs.value = songs
            _error.value = false
        }else{
            _error.value = true
        }
    }
}