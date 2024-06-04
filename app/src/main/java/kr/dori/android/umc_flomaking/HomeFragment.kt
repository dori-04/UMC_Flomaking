package kr.dori.android.umc_flomaking

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kr.dori.android.umc_flomaking.databinding.FragmentHomeBinding

//부모 클래스로 Fragment()를 가져와야 다른 엑티비티나 프래그먼트에서 참조할 수 있다. Activity 설정을 따라가면 안 된다.
class HomeFragment: Fragment(){
    lateinit var binding: FragmentHomeBinding
    lateinit var mainActivity: MainActivity
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater,container, false)

        //data가 비교적 적어서 Of를 이용해서 직접 입력했으나 보통은 함수를 이용한다.
        val data = mutableListOf(
            HomeList(R.drawable.img_album_legend1,"매혹적인 여성의 \n음색"),
            HomeList(R.drawable.img_album_legend2,"매혹적인 두명의 \n여성의 음색"),
            HomeList(R.drawable.img_album_legend3, "매혹적인 세명의 \n여성의 음색")
        )
        var adapter = CustomViewAdapter() //어댑터 인스턴스화
        adapter.dataList = data //어댑터로 아이템뷰와 데이터 연결하기
        binding.homeContentVp.adapter =adapter //홈 프래그먼트에 있는 VP의 어댑터 설정

        binding.indicator.setViewPager(binding.homeContentVp) //인디케이터 설정, adapter만 리사이클러뷰어뎁터이고 실제 뷰는 뷰페이저여서 뷰페이저 설정을 따라간다.


        binding.homeAlbumImgIv1.setOnClickListener{
            mainActivity.goAlbum()
        }
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mainActivity = context as MainActivity
    }

}