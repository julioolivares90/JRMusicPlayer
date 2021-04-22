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
import com.julioolivares.musicplayer.viewModels.ListMusicViewModel
import org.koin.android.ext.android.inject
import java.time.Duration


class ListMusicFragment : Fragment(R.layout.fragment_list_music) {


    private  var _binding : FragmentListMusicBinding? = null

    private val binding get() = _binding!!

    private var sonList : MutableList<Song> = ArrayList()

    private lateinit var songAdapter: SongAdapter

    private  val viewModel: ListMusicViewModel by inject()

    @RequiresApi(Build.VERSION_CODES.O)
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
        checkUserPermission()

        //loadSongs()
        setupRecyclerView()
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
                    //activity?.toast("Permissions Granted")
                        viewModel.getAllSons()
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
       // activity?.toast("Granted")
        viewModel.getAllSons()
        loadSongs()
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun loadSongs() {
        viewModel.sons().observe(viewLifecycleOwner, {songs ->
            sonList.addAll(songs)
        })
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