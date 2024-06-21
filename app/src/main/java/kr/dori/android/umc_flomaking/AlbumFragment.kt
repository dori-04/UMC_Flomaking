package kr.dori.android.umc_flomaking

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import kr.dori.android.umc_flomaking.databinding.FragmentAlbumBinding
import kr.dori.android.umc_flomaking.databinding.FragmentAlbumDetailBinding
import kr.dori.android.umc_flomaking.databinding.FragmentHomeBinding

//Activity와는 다르게 Fragment Context를 상속받는다.
class AlbumFragment: Fragment() {
    var mainActivity: MainActivity? = null //미리 전역으로 변수 설정해두자.
    lateinit var binding: FragmentAlbumBinding
    private var gson: Gson = Gson()

    //Fragment에서 onCreate 대신에 onCreateView를 사용하는 이유는 다음과 같다.
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =FragmentAlbumBinding.inflate(inflater,container,false)

        val albumJson = arguments?.getString("album_key")
        val album = gson.fromJson(albumJson, Album::class.java)
        setinit(album)



        //mainAcitivity에 있는 goHome메소드 사용
        binding.albumBackIv.setOnClickListener{
            mainActivity?.goHome()
        }


        //뷰페이저와 탭 레이아웃 설정, <<이것이 안드로이드다>>에서처럼 데이터를 어댑터가 아닌 실행하는 Fragment에서 직접 입력해도 된다.
        val albumAdapter = AlbumVPAdapter(this)
        val information = arrayListOf("수록곡","상세정보","영상")
        binding.albumContentVp.adapter = albumAdapter
        TabLayoutMediator(binding.albumContentIb,binding.albumContentVp){
            tab,position->
            tab.text = information[position]
        }.attach()

        return binding.root //이거보다 아래줄에 적으면 binding이 반영이 안된다.
    }

    override fun onAttach(context: Context) { //MainActivity를 받아와서 메소드를 사용할 수 있도록 한다.
        super.onAttach(context)

        if(context is MainActivity)mainActivity = context
    }

    private fun setinit(album:Album){
        binding.albumAlbumIv.setImageResource(album.coverImg!!)
        binding.albumMusicTitleTv.text=album.title.toString()
        binding.albumSingerNameTv.text = album.singer.toString()
    }
}