package kr.dori.android.umc_flomaking

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kr.dori.android.umc_flomaking.databinding.ActivitySongBinding

//AppCompatActivity = 안드로이드에서 엑티비티의 기능을 사용할 수 있도록 제공해주는 컨텍스트 중 하나

class SongActivity: AppCompatActivity() {
    lateinit var binding: ActivitySongBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //인플레이트는 xml 파일을 객체화시켜서 kt코드에서 사용할 수 있도록 하는 방법이다.
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)//Ctrl 클릭하면 해당 뷰를 볼 수 있는데, root를 클릭해보면 root가 전체 레이아웃을 포함한다는 것을 알 수 있다.

        binding.songDownIb.setOnClickListener {
            finish() //Main -> Song으로 바뀌는 과정은 스택을 통해서 이루어 진다. 따라서 현재 Song 스택을 제거하기만 하면 Main이 다시 보이게 되는 것
        }

        //MainActivity의 미니플레이어에서 노래제목과 가수 정보를 가져와서 세팅하는 방법
        if(intent.hasExtra("title")&&intent.hasExtra("singer")){
            binding.songMusicTitleTv.text = intent.getStringExtra("title")
            binding.songSingerNameTv.text = intent.getStringExtra("singer")
        }
    }
}