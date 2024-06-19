package kr.dori.android.umc_flomaking

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import kr.dori.android.umc_flomaking.databinding.ActivitySplashBinding


class SplashActivity:AppCompatActivity() {

    lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val handler = Handler(Looper.getMainLooper())

        handler.postDelayed({ //기본 문법은 handler.post인데 delay를 넣음으로서 splash 효과를 낼 수 있다.
            startActivity(Intent(this,MainActivity::class.java))

        },1000)
    }

}