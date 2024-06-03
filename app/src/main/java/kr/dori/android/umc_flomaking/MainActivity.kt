package kr.dori.android.umc_flomaking

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import kr.dori.android.umc_flomaking.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    val binding by lazy {ActivityMainBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContentView(binding.root)

        initBottomNavigation()
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
}