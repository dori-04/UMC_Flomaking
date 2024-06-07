package kr.dori.android.umc_flomaking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kr.dori.android.umc_flomaking.databinding.FragmentBannerBinding

class BannerFragment(val imgRes: Int): Fragment() {
    //파라미터에 val를 넣어야 메소드나 프로퍼티에서 바로 갖다 쓸 수 있다.
    lateinit var binding : FragmentBannerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentBannerBinding.inflate(inflater, container, false)

        binding.bannerImgIv.setImageResource(imgRes)
        return binding.root
    }
}