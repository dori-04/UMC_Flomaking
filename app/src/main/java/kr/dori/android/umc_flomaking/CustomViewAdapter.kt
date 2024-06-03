package kr.dori.android.umc_flomaking

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.dori.android.umc_flomaking.HomeList
import kr.dori.android.umc_flomaking.databinding.ItemLayoutBinding

//같은 구조의 레이아웃이 반복될 때는 리사이클러뷰를 상속받는게 이득이다. 뷰페이저를 사용할 경우 뷰홀더를 만들지 않아도 되지만 프래그먼트를 졸라 만들어야 된다.
class CustomViewAdapter(): RecyclerView.Adapter<Holder>() {
    var dataList = listOf<HomeList>() //이 어댑터에서 사용할 데이터 목록 변수를 선언하기 -> 빈 리스트는 항상 생성과 동시에 타입 명시하기

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder { //layout 생성해서 Holder에게 전달해주기
        val binding = ItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) { //화면에 보여주는 함수
        val data = dataList[position] //position값이 파라미터이므로 사용 가능
        holder.setText(data)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}

//여기서도 리사이클러뷰 상속받기, 뷰홀더를 통해서 자원을 효율적으로 운용할 수 있다.
class Holder(val binding: ItemLayoutBinding): RecyclerView.ViewHolder(binding.root){ //어댑터가 생성한 바인딩 레이아웃을 Holder에서 받아서 부모 클래스에 전달한다.

    fun setText(data: HomeList){ //어댑터에서 제공받은 레이아웃과 Main에서 인스턴스화 시킬 때 제공받는 데이터를 여기서 합친다.

        binding.homePannelTitleTv.text = data.title
        Glide.with(itemView).load(data.backgroundimg).into(binding.homePannelBackgroundIv)
    }
}











