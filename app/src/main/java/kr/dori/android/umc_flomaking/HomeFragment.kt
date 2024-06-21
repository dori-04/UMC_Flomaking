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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import kr.dori.android.umc_flomaking.databinding.FragmentHomeBinding

//부모 클래스로 Fragment()를 가져와야 다른 엑티비티나 프래그먼트에서 참조할 수 있다. Activity 설정을 따라가면 안 된다.
class HomeFragment: Fragment(){
    lateinit var binding: FragmentHomeBinding
    lateinit var mainActivity: MainActivity
    private var albumData = ArrayList<Album>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater,container, false)
        // 이 부분은 HomeBackgroundIMG를 리사이클러뷰 어댑터와 뷰페이저를 사용해서 구현한 부분
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

/*
실습보다 컨텍스트(메인 엑티비티)를 좀 더 활용했다.
horizontal Scroll layout에서 recyclerView로 바꾸면서 click event를 adapter에서 설정해야 하기에 주석처리함
        binding.homeAlbumImgIv1.setOnClickListener{
            mainActivity.goAlbum()
        }
*/
        //데이터 리스트 생성 더미 데이터
        albumData.apply {
            add(Album("Butter","방탄소년단",R.drawable.img_album_exp))
            add(Album("Lilac","아이유",R.drawable.img_album_exp2))
            add(Album("Next Level","에스파",R.drawable.img_album_exp3))
            add(Album("Boy With Luv","방탄소년단",R.drawable.img_album_exp4))
            add(Album("BBoom BBoom","모모랜드",R.drawable.img_album_exp5))
            add(Album("Weekend","태연",R.drawable.img_album_exp6))

        }

        //리사이클러뷰 어댑터 설정 및 레이아웃 설정
        //homeContentViewPager에서는 class 참조변수로 data변수를 만들어서 class 인스턴스화 시키고 나서 class.변수 = albumdata식으로 했으나 아래와 같이 class 파라미터로 넣을 수 있다.
        val albumRVAdapter = AlbumRVAdapter(albumData)
        binding.homeTodayMusicAlbum2Rv.adapter = albumRVAdapter
        binding.homeTodayMusicAlbum2Rv.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)

        //object로 익명객체 생성 -> MyItemClickListener의 메소드인 onItemClick을 바로 오버라이드 시킬 수 있다, setMy~메소드의 파라미터로 MyItem~인터페이스를 익명객체로 생성했다.
        albumRVAdapter.setMyItemClickListener(object : AlbumRVAdapter.MyItemClickListener{
            override fun onItemClick(album: Album) {
                mainActivity.album = album
                mainActivity.goAlbum() //여기서 클릭 리스너 구현하기
            }

            override fun onRemoveAlbum(position: Int) {
                albumRVAdapter.removeItem(position)
            }
        })




        //배너 구현 부분 (이후에 endless viewpager로 대체할 것)
        val BannerAdapter = BannerVPAdapter(this)
        BannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        BannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        BannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        BannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        BannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        BannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        binding.homeBannerVp.adapter = BannerAdapter
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        return binding.root
    }

    override fun onAttach(context: Context) { //컨텍스트 가져오기, 생명주기에서 가장 먼저 호출된다.
        super.onAttach(context)

        mainActivity = context as MainActivity
    }



}