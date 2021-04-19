package com.julioolivares.musicplayer.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.julioolivares.musicplayer.databinding.SongBinding
import com.julioolivares.musicplayer.fragments.ListMusicFragmentDirections
import com.julioolivares.musicplayer.models.Song

class SongAdapter  : ListAdapter<Song,SongAdapter.SongHolder>(differCallback) {

    lateinit var binding: SongBinding

    class SongHolder (val songBinding: SongBinding) : RecyclerView.ViewHolder(songBinding.root) {
        fun bind (currentSong : Song){
            songBinding.songTitle.text = currentSong.songTitle
            songBinding.songArtist.text = currentSong.songArtist
            songBinding.tvDuration.text = currentSong.songDuration
            songBinding.tvOrder.text = "$position + 1"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongHolder {

        binding = SongBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return  SongHolder(binding)
    }

    override fun onBindViewHolder(holder: SongHolder, position: Int) {

        val currentSong = getItem(position)

        holder.bind(currentSong = currentSong)

        holder.itemView.setOnClickListener {mView ->
            val direction = ListMusicFragmentDirections
                    .actionListMusicFragmentToMusicPlayFragment(currentSong)
            mView.findNavController().navigate(direction)
        }

    }
}

private object  differCallback :  DiffUtil.ItemCallback<Song>() {
    override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
        return oldItem.songTitle == newItem.songTitle &&
                oldItem.songArtist == newItem.songArtist &&
                oldItem.songDuration == newItem.songDuration &&
                oldItem.songUri == newItem.songUri
    }

    override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
        return oldItem == newItem
    }

}