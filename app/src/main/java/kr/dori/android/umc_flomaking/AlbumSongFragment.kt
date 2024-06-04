package kr.dori.android.umc_flomaking

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kr.dori.android.umc_flomaking.databinding.FragmentAlbumSongBinding


class FragmentAlbumSong : Fragment() {
    lateinit var binding: FragmentAlbumSongBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumSongBinding.inflate(inflater,container,false)

        binding.songLalacLayout.setOnClickListener{
            Toast.makeText(activity,"Lilac",Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

}