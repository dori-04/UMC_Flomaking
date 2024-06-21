package kr.dori.android.umc_flomaking

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.SurfaceControl.Transaction
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import kr.dori.android.umc_flomaking.databinding.ActivityMainBinding

//song은 클래스의 멤버변수이므로 모든 메서드에서 접근이 가능하다. 흐름을 살펴보자면 onCreate()에서 Activitity의 레이아웃을 구성하고 song의 디폴트값으로 UI구성을 한다. 이후에 onStart()에서 SharedPreferences에서 "song"데이터를 불러와서 'song' 변수를 업데이트하고 setMiniPlayer(song)을 호출하여 UI를 업데이트한다.
class MainActivity : AppCompatActivity() {
    val binding by lazy {ActivityMainBinding.inflate(layoutInflater)}
    private var song: Song = Song()
    private val gson: Gson = Gson()
    lateinit var album: Album

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()dwqa
        setContentView(binding.root)

        initBottomNavigation()
        //메인플레이어의 곡 정보와 SongFragment의 곡 정보를 같게 하기 위한 설정이다. 데이터 타입으로는 직접 설정한 Song 데이터 클래스를 사용한다.

//Gson 추가하면서 삭제시킴
//        val song = Song(binding.mainMiniplayerTitleTv.text.toString(), binding.mainMiniplayerSingerTv.text.toString(),0,60,false,"siren")
//        Log.d("Song",song.title+ song.singer)


        binding.mainPlayerCl.setOnClickListener{//Activity간 통신
            val intent = Intent(this,SongActivity::class.java)
            intent.putExtra("title",song.title)
            intent.putExtra("singer",song.singer)
            intent.putExtra("second",song.second)
            intent.putExtra("playTime",song.playTime)
            intent.putExtra("isPlaying",song.isPlaying)
            intent.putExtra("music", song.music)
            startActivity(intent)
        }



    }
/*
-FragmentHome과 AlbumFragment에서 MainActivity를 컨텍스트로 참조하기 때문에 관련 메소드들을 적어둔다. 여기서 적은 메소드는 앞 두 프래그먼트에서 onAttach 메소드로 MainActivity를 불러오면 사용할 수 있다.
-참고로 기본 fragment 세팅은 바텀네비게이션뷰에서 진행함
-goAlbum()메소드를 통해서 넘어갈 때 즉 AlbumFragment()가 처음 시작할 때 정보를 전달하는 방법으로는 argument를 사용할 수 있다.
-이때 argument에 넣을 데이터로는 album데이터 더미가 필요한데, 이는 HomeFragment에서 관리하므로 미리 전역변수로 album을 만든다음에 Homefragment의 onItemClick메소드에서 초기화시킨다.


 */
    fun goAlbum(){

        val albumFragment = AlbumFragment()
        val transaction = supportFragmentManager.beginTransaction() //프래그먼트는 트랜잭션으로 관리하여 안정성을 높인다. 여러 의존성이 모여있을 때 사용한다.
        transaction.replace(R.id.main_frm, albumFragment)
        albumFragment.apply{
            arguments = Bundle().apply{
                val gson = Gson()
                val albumJson = gson.toJson(album)
                putString("album_key",albumJson)
            }
        }
        //transaction.addToBackStack("gimozzi") 프래그먼트를 하나의 스택으로 관리한다. -> 엑티비티처럼
        transaction.commit()
    }

    fun goHome(){
        val homeFragment = HomeFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_frm, homeFragment) //원래 add로 했는데 스택을 너무 많이 차지하는 것 같아서 replace로 교체
        transaction.commit()
    }



    private fun initBottomNavigation(){
        //참고: https://stackoverflow.com/questions/45686090/how-to-set-default-bottomnavigationview-tab-in-kotlin
        //바로 아래는 bottomNavigationView의 초기 버튼 설정하는 방법
        //여기서의 id는 btm_color_selector의 item id이다.
        val bottomNavigationView: BottomNavigationView= findViewById(R.id.main_bnv) as BottomNavigationView
        bottomNavigationView.selectedItemId=R.id.lookFragment

        //여기는 초기 Fragment설정이다.
        //참고: https://medium.com/hongbeomi-dev/%EB%B2%88%EC%97%AD-%EB%8B%A4%EC%96%91%ED%95%9C-%EC%A2%85%EB%A5%98%EC%9D%98-commit-8f646697559f
        //.commit을 사용하면 onSavedInstanceState() 이후의 커밋은 onRestart에서 반영되지 않기 때문에 오류가 발생한다.
        //.commitAllowingStateLoss()는 변경점을 날려서 이를 해결한다. 오류가 생기려면 onSavedInstanceState() 생명주기에 포함시켜야 한다. 따라서 지금 커밋(깃)상태에서는 .commit()으로 바꿔도 무방하다.
        supportFragmentManager.beginTransaction()
            .add(R.id.main_frm, LookFragment())
            .commitAllowingStateLoss()

        binding.mainBnv.setOnItemSelectedListener{ item ->
            when (item.itemId) {

                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, HomeFragment())
                        .commitAllowingStateLoss()
                    //  return@setOnItemSelectedListener true
                }

                R.id.lookFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LookFragment())
                        .commitAllowingStateLoss()
                    //  return@setOnItemSelectedListener true
                }
                R.id.searchFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, SearchFragment())
                        .commitAllowingStateLoss()
                    //   return@setOnItemSelectedListener true
                }
                R.id.lockerFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LockerFragment())
                        .commitAllowingStateLoss()
                    // return@setOnItemSelectedListener true
                }
            }
            true
        }

    }

    private fun setMiniPlayer(song:Song){
        binding.mainMiniplayerProgressSb.progress = (song.second*100000)/song.playTime
        binding.mainMiniplayerTitleTv.text = song.title
        binding.mainMiniplayerSingerTv.text = song.singer
    }

    override fun onStart(){ //SongActivity로 갔다가 돌아오는 경우에 onStart()부터 시작한다. onResume()을 이용할 수도 있지만 그러면 화면 그려지는 시점이 뒤로 가서 불편하다.
        super.onStart()
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val songJson = sharedPreferences.getString("songData",null)

        song = if(songJson == null){ //초기값 설정 어차피 onCreate 다음에 onStart
            Song("SIREN","HOMI",0,60,false,"siren")
        }else{ //json객체가 있다면 초기화
            gson.fromJson(songJson, Song::class.java)
        }

        setMiniPlayer(song)
    }

}













