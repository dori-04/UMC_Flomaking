package kr.dori.android.umc_flomaking

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class BannerVPAdapter(fragment: Fragment):FragmentStateAdapter(fragment) {
    private val fragmentlist : ArrayList<Fragment> = ArrayList()
    override fun getItemCount(): Int = fragmentlist.size //코틀린은 기본적으로 함수형 언어다.

    override fun createFragment(position: Int): Fragment = fragmentlist[position]

    fun addFragment(fragment: Fragment){
        fragmentlist.add(fragment)
        notifyItemInserted(fragmentlist.size -1 ) //0부터 시작하니까 ex) 3개인 list에 4번째 데이터를 추가하면 position은 size -1인 3으로 들어가게 된다.
    }
}