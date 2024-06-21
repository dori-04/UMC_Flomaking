package kr.dori.android.umc_flomaking

import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.provider.MediaStore.Audio.Media
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import kr.dori.android.umc_flomaking.databinding.ActivitySongBinding

//AppCompatActivity = 안드로이드에서 엑티비티의 기능을 사용할 수 있도록 제공해주는 컨텍스트 중 하나

class SongActivity: AppCompatActivity() {
    lateinit var binding: ActivitySongBinding
    lateinit var song : Song
    lateinit var timer: Timer
    private var mediaPlayer: MediaPlayer? = null //nullable 처리하는 이유는 onDestroy때 mediaPlayer 데이터를 소멸할 것이기 때문이다
    private var gson: Gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //인플레이트는 xml 파일을 객체화시켜서 kt코드에서 사용할 수 있도록 하는 방법이다.
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)//Ctrl 클릭하면 해당 뷰를 볼 수 있는데, root를 클릭해보면 root가 전체 레이아웃을 포함한다는 것을 알 수 있다.

        initSong()
        startTimer()
        setPlayer(song)


        binding.songDownIb.setOnClickListener {
            finish() //Main -> Song으로 바뀌는 과정은 스택을 통해서 이루어 진다. 따라서 현재 Song 스택을 제거하기만 하면 Main이 다시 보이게 되는 것
        }
        binding.songMiniplayerIv.setOnClickListener {
            setPlayerStatus(true) //View.GONE 상태에서는 뷰의 공간 자체가 사라지는 것이므로 클릭 이벤트 또한 실행할 수 없다. 따라서 이와 같은 코딩이 가능하다.
        }
        binding.songPauseIv.setOnClickListener {
            setPlayerStatus(false)
        }

    }

    //MainActivity에서 intent로 전달한 내용 받아오기
    private fun initSong(){
        if(intent.hasExtra("title")&&intent.hasExtra("singer")){
            song=Song(
                intent.getStringExtra("title")!!, //!!는 null 안정성을 담보하지 않는 문법 값이 있을 것을 담보하는 상황이다.
                intent.getStringExtra("singer")!!,
                intent.getIntExtra("second",0),
                intent.getIntExtra("playTime",0),
                intent.getBooleanExtra("isPlaying",false),
                intent.getStringExtra("music")!!
            )
        }
        //음원 파일을 받아오기 위해서 resource 객체의 getIdentifier 메소드를 사용한다.
        val music = resources.getIdentifier(song.music,"raw",this.packageName)
        //받아온 음원 파일을 mediaPlayer에게 전달시켜서 재생시킨다.
        mediaPlayer = MediaPlayer.create(this,music)
        song.playTime = mediaPlayer?.duration?:0 //?:는 엘비스로서 널값일 때 사용할 기본값을 설정할 수 있다.
        if(song.playTime != null){ //duration은 mills 단위이므로 second로 치환해준다.
            song.playTime = song.playTime/1000 //Timer Thread를 실행하기 전에 미리 song 데이터를 초기화시켜야 한다.
        }
    }

    private fun setPlayer(song:Song){ //기본 UI 설정
        binding.songMusicTitleTv.text=intent.getStringExtra("title")!!
        binding.songSingerNameTv.text=intent.getStringExtra("singer")!!
        binding.songProgressTimeTv.text=String.format("%02d:%02d",song.second/60, song.second%60) //StartTime보다는 progressTime이 더 적절하지 않을까 싶은 부분 그래서 바꿈
        binding.songEndTimeTv.text=String.format("%02d:%02d",song.playTime/60, song.playTime%60)
        binding.songProgressSb.progress=(song.second*1000/song.playTime)

        setPlayerStatus(song.isPlaying)
    }

    private fun setPlayerStatus(isPlaying:Boolean){ //접근 지정자 private은 같은 클래스 내부에서만 사용할 수 있게 해준다.
        song.isPlaying = isPlaying //onCreate에 설정한 클릭이벤트에서 boolean값을 받아와서 전역변수와 연동시킨다. 전역변수로 timer thread의 동작을 제어할 수 있다.
        timer.isPlaying = isPlaying //thread는 굉장히 짧은 텀을 두고서 교차하기 때문에 thread를 실행시켜두고 계속 isPlaying상태를 업데이트 시켜줄 수 있다.

        if(isPlaying){
            binding.songMiniplayerIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
            mediaPlayer?.start()
        }else{
            binding.songMiniplayerIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE
            if(mediaPlayer?.isPlaying == true){ //재생 중이 아닐 때 pause 걸면 앱이 멈춘다.
                mediaPlayer?.pause()
            }
        }
    }

    private fun startTimer(){
        timer = Timer(song.playTime, song.isPlaying)
        timer.start()
    }

    //java와 달리 class 내부에 class를 생성해도 별도의 class로 구분 내부클래스는 inner 키워드를 사용해야 하고 이렇게 해야 부모클래스에 접근이 가능하다.
    inner class Timer(private val playTime: Int, var isPlaying: Boolean = true):Thread(){
        private var second: Int = 0
        private var mills: Float = 0f

        override fun run(){
            super.run()
            try{
                while(true){
                    if(second >= playTime){
                        break
                    }
/*
- runOnUiThread와 Handler의 공통점은 모두 background thread에서 처리할 수 없는 UI작업을 MainThread에서 처리하기 위해 사용되는 함수이다. runOnUiThread는 현재 Thread가 mainThread인지 확인하고 아니라면 main으로 보내서 작업을 처리한다. 또한 굉장히 직관적이다. Handler는 단방향 통신은 아니긴 한데, background에서 UI작업을 위해서 main에다가 runnable객체와 massage를 보내는 역할을 수행한다. 명시적으로 작업해야 될 것들이 많은 편이고 스레드를 개발자가 직접 구분해야 한다. 복잡하고 세밀한 작업일수록 유리하다. 아래 코딩은 간단하므로 runOnUiThread로 진핸한다.
-++연산자를 알아보자. x++는 변수를 증가시킨 후에 그 값을 반환하고 ++x는 값을 반환한 후에 증가시킨다. 전자를 전위 연산자 후자를 후위 연산자라고 함
-+=는 잘 아니까 패스
-.inc()는 변수의 값에서 1을 더한 값을 반환하지만 변수 자체의 값은 바꾸지 않는다.
-아래 코드를 second.inc()로 했었는데, second값 자체를 바꾸지는 않으니까 시간이 흐르지 않는 상태가 발생했다.
 */

                    if(isPlaying){
                        sleep(50)
                        mills+=50
                        runOnUiThread{ //progressbar는 mill단위로 반영하기
                            binding.songProgressSb.progress = ((mills/playTime)*100).toInt()
                        }
                        if(mills%1000 == 0f){ //progressTime은 mill단위로 반영하면 정신 안 좋기에 second단위로 업데이트
                            runOnUiThread{
                                binding.songProgressTimeTv.text=String.format("%02d:%02d",second/60,second%60)
                            }

                            second+=1
                        }
                    }
                }
//try는 예외가 발생할 수 있는 코드를 작성한다. catch는 예외가 발생했을 때 처리할 코드를 설정한다.
            }catch (e:InterruptedException){
                Log.d("Song","쓰레드 사망.${e.message}")
            }

        }
    }

/*
- 사용자가 포커스를 잃었을 때 음악이 중지됩니다.
-기본적으로 kotlin에서 부모클래스는 자식클래스에 대한 접근이 차단되어있다. 인스턴스화를 시키지 않는다면.. 그래서 Timer 내부에 사용된 second변수를 사용할 수 없으므로 아래와 같이 부모클래스의 second변수를 초기화시켜서 저장해야 한다.
-mills를 second로 만들기 위해서 1000으로 나누었고 100으로 나눈 이유는 초기 progress 설정값에 맞춰주기 위함이다.
-앱 종료 후에도 곡 정보와 진행상황을 저장하기 위해서 안드로이드 내부 저장소인 sharedPreference를 사용한다.
-intent와 같이 put~ 형태로 데이터를 넣을 수도 있지만 json을 이용해 보도록 하자. json은 데이터 형식 그 자체이고 gson은 json데이터를 java객체로 바꾸거나(역직렬화) 그 반대로(직렬화) 하기 위해서 만든 라이브러리이다. (구글에서 만들어서 g가 붙음)
 */
    override fun onPause(){
        super.onPause()
        setPlayerStatus(false)
        song.second = ((binding.songProgressSb.progress * song.playTime)/100)/1000
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE) //데이터 저장소 자체의 이름
        val editor = sharedPreferences.edit() //에디터
        val songJson = gson.toJson(song)
        editor.putString("songData",songJson)
        editor.apply() //git에서 commit&push라고 생각
    }

        override fun onDestroy() {
        super.onDestroy()
        timer.interrupt() //이 설정을 하지 않으면 SongActivity를 종료하고 나서도 Timer Thread가 계속 돌아가면서 자원을 낭비한다.
        mediaPlayer?.release() //마찬가지로 자원 낭비 방지
    }
}