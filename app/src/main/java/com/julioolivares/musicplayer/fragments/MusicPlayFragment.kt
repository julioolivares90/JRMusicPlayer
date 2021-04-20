package com.julioolivares.musicplayer.fragments

import android.content.ContentUris
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.navArgs
import com.julioolivares.musicplayer.R
import com.julioolivares.musicplayer.databinding.FragmentMusicPlayBinding
import com.julioolivares.musicplayer.helper.Constants
import com.julioolivares.musicplayer.helper.Constants.toast
import com.julioolivares.musicplayer.models.Song
import java.io.File


class MusicPlayFragment : Fragment(R.layout.fragment_music_play) {

    private lateinit var _binding: FragmentMusicPlayBinding

    private val binding get() = _binding


    private val args : MusicPlayFragmentArgs by navArgs()

    private lateinit var song : Song

    private  var mediaPlayer: MediaPlayer? = null

    private var seekLength : Int =0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMusicPlayBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mediaPlayer = MediaPlayer()
        song = args.song!!

        binding.tvAuthor.text = song.songTitle
        binding.tvTitle.text = song.songTitle
        binding.tvDuration.text = song.songDuration

        binding.ibPlay.setOnClickListener {
            playSong()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        clearMediaPlayer()
        
    }
    private fun playSong() {
        if (!mediaPlayer!!.isPlaying){
            mediaPlayer!!.reset()
            val id: Long = song.idSong

            val contentUri : Uri =
                    ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,id)


            mediaPlayer!!.setDataSource(requireContext().applicationContext,contentUri)//siempre primero este  seguir este orden
            mediaPlayer!!.prepare() //para archivos que son de internet usar la version async de este metodo
            mediaPlayer!!.seekTo(seekLength)
            mediaPlayer!!.start()

            binding.ibPlay.setImageDrawable(
                    ContextCompat.getDrawable(
                            activity?.applicationContext!!,
                            R.drawable.ic_pause
                    )
            )
            updateSeekbar()
        }else {

            mediaPlayer!!.pause()
            seekLength = mediaPlayer!!.currentPosition
            binding.ibPlay.setImageDrawable(
                    ContextCompat.getDrawable(
                            activity?.applicationContext!!,
                            R.drawable.ic_play
                    )
            )
        }

    }

    private fun updateSeekbar(){
        if (mediaPlayer != null){
            binding.tvCurrentTime.text = Constants.durationConverter(
                    mediaPlayer!!.currentPosition.toLong()
            )
        }

        seekBarSetUp()
        Handler().postDelayed(runnable,50)
    }

    private var runnable = Runnable {
        updateSeekbar()
    }
    private fun seekBarSetUp() {
        if (mediaPlayer != null){
            binding.seekBar.progress = mediaPlayer!!.currentPosition
            binding.seekBar.max = mediaPlayer!!.duration
        }

        binding.seekBar.setOnSeekBarChangeListener(
                object : SeekBar.OnSeekBarChangeListener{
                    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                        if (fromUser){
                            mediaPlayer!!.seekTo(progress)
                            binding.tvCurrentTime.text =
                                    Constants.durationConverter(progress.toLong())
                        }
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {
                        if (mediaPlayer != null && mediaPlayer!!.isPlaying){
                            if (seekBar != null){
                                mediaPlayer!!.seekTo(seekBar.progress)
                            }
                        }
                    }

                }
        )

    }

    private fun clearMediaPlayer(){
        if (mediaPlayer!!.isPlaying)
            mediaPlayer!!.stop()
        mediaPlayer!!.release()
    }
}