package com.julioolivares.musicplayer.fragments

import android.content.ContentResolver
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.julioolivares.musicplayer.R
import com.julioolivares.musicplayer.adapters.SongAdapter
import com.julioolivares.musicplayer.databinding.FragmentListMusicBinding
import com.julioolivares.musicplayer.helper.Constants
import com.julioolivares.musicplayer.helper.Constants.toast
import com.julioolivares.musicplayer.models.Song
import java.time.Duration


class ListMusicFragment : Fragment(R.layout.fragment_list_music) {


    private  var _binding : FragmentListMusicBinding? = null

    private val binding get() = _binding!!

    private var sonList : MutableList<Song> = ArrayList()

    private lateinit var songAdapter: SongAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentListMusicBinding.inflate(inflater,container,false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadSongs()
        setupRecyclerView()
        checkUserPermission()

    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            Constants.REQUEST_CODE_FOR_PERMISSIONS ->
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    activity?.toast("Permissions Granted")
                    loadSongs()
                }else {
                    activity?.toast("Permissions Denied")
                }else -> {
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
                }
        }
    }
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun checkUserPermission(){
        if (activity?.let {
                    ActivityCompat.checkSelfPermission(
                            it,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE,

                    )
                } != PackageManager.PERMISSION_GRANTED && activity?.let {
                    ActivityCompat.checkSelfPermission(it,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                } != PackageManager.PERMISSION_GRANTED){

            requestPermissions(
                    arrayOf(
                            android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ),
                    Constants.REQUEST_CODE_FOR_PERMISSIONS
            )
            return
        }
        activity?.toast("Granted")
        loadSongs()
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun loadSongs() {
        //val resolver : ContentResolver = activity?.applicationContext!!.contentResolver
        val allSongURI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val selection = MediaStore.Audio.Media.IS_MUSIC + "!=0"

        val sortOrder = "${MediaStore.Audio.Media.DISPLAY_NAME} ASC"

        val cursor = activity?.applicationContext?.contentResolver!!.query(
                allSongURI,null,selection,null,sortOrder
        )

        when {
            cursor == null -> {
                context?.toast("Ocurrio un error")
            }

            !cursor.moveToFirst() -> {
                context?.toast("No se encontraron archivos en el dispositivo")
            }else -> {
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
                    sonList.add(
                            song
                    )
                }while (cursor.moveToNext())
            }
        }
        /*
        if (cursor != null){
            while (cursor.moveToNext()){
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
                        songDuration = Constants.durationConverter(songDurlong)
                )
                sonList.add(
                      song
                )
            }
        }
         */

        cursor?.close()

    }

    private fun checkDuration(songDuration: String?) : Long{
        return songDuration?.toLong() ?: 0L
    }
    private fun setupRecyclerView(){
        songAdapter = SongAdapter()

        binding.rvSongList.apply {
            adapter = songAdapter
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)
            addItemDecoration(object : DividerItemDecoration(
                    activity,LinearLayoutManager.VERTICAL
            ){})
        }

        songAdapter.submitList(sonList)

        sonList.clear()
    }
}