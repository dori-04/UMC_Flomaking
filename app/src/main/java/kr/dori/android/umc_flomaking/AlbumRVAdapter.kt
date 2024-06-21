package kr.dori.android.umc_flomaking

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.dori.android.umc_flomaking.databinding.ItemAlbumBinding

/*
RecyclerView 작동 순서 요약

1. XML 레이아웃 인플레이트: XML 파일에 정의된 RecyclerView 레이아웃을 인플레이트 합니다.
2. LayoutManager 설정: Activity나 Fragment에서 LayoutManager를 설정하여 아이템들이 화면에 어떻게 배치될지 결정합니다.
3. RecyclerView와 Adapter 연결: Activity나 Fragment에서 RecyclerView와 Adapter를 연결합니다.
4. ViewHolder 생성: onCreateViewHolder 메소드를 통해 새로운 ViewHolder 객체를 생성하고, 아이템 레이아웃을 인플레이트 합니다.
5. 데이터와 뷰 바인딩: onBindViewHolder 메소드를 통해 ViewHolder의 뷰에 데이터를 바인딩합니다.
6. 뷰 재사용: ViewHolder를 통해 아이템 뷰를 재사용합니다. 이는 스크롤 시 화면에서 벗어난 뷰를 재활용하여 성능을 최적화합니다.

클릭 이벤트 관련해서
# 데이터나 메소드는 HomeFragment에 AlbumLayout은 AlbumFragment에 HomeFragment에서 참조하는 context는 MainActivity라는 점을 이해해야 합니다.
1. MyItemClickListener 인터페이스와 MyItemClickListener를 사용하기 위한 변수(myItemClickListener)를 AlbumRVAdapter에 정의합니다.
2. HomeFragment에서 MyItemClickListener를 마저 구현합니다. 이때 MainActivity의 Context를 참고합니다.
3. MainActivity에서 정의한 goAlbum()메소드를 수정합니다. AlbumFragment로 넘어갈 때 데이터도 전달해야 되기 때문에 argument를 이용하고 album 데이터를 이용하기에 미리 전역변수로 정의합니다.
4. AlbumFragment에서 argument를 이용하여 데이터 바인딩 작업을 마무리합니다.
5. 이 과정을 item 객체마다 지정해야 하기 때문에 AlbumRVAdapter의 onBindViewHolder에 ClickListener를 position과 함께 정의합니다.
# HomeFragment에서 setMyItemClickListener의 파라미터로 재 정의한 인터페이스를 넣었기 때문에 AlbumRVAdapter에서도 업데이트된 setMyItemClickListener를 사용할 수 있게 됩니다.

추가 설명

- onCreateViewHolder: 새로운 뷰를 생성할 때 호출되며, LayoutInflater를 사용해 아이템 레이아웃을 인플레이트 합니다. 인플레이트된 레이아웃을 보유하는 ViewHolder 객체를 반환합니다.
- onBindViewHolder: 재사용할 수 있는 뷰를 데이터와 바인딩합니다. 주어진 위치의 데이터를 가져와서 ViewHolder의 뷰에 설정합니다.
- 뷰 재사용: 리사이클러뷰는 화면에서 벗어난 뷰를 재사용하여 메모리와 성능을 최적화합니다. ViewHolder 패턴을 통해 불필요한 findViewById 호출을 줄이고, 스크롤 성능을 향상시킵니다.
-onBindViewHolder에서 사용된 itemView는 ViewHolder가 담고 있는 아이템 객체들을 의미합니다. 즉, Adapter가 참조한 itemLayout의 전체를 참조합니다.
*/

class AlbumRVAdapter(private val albumList: ArrayList<Album>): RecyclerView.Adapter<AlbumRVAdapter.ViewHolder>(){

    // 인터페이스는 구현은 하지 않으면서 하위 클래스에서 사용할 메소드에 대한 가이드만 주는 역할 -> 코드를 구조화시키고 일관성 있게 만들어준다.
    interface MyItemClickListener{
        fun onItemClick(album: Album)
        fun onRemoveAlbum(position: Int)
    }

    private lateinit var myItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        myItemClickListener = itemClickListener
    }

    // onCreateViewHolder: 새로운 뷰를 생성할 때 호출되며, LayoutInflater를 사용해 아이템 레이아웃을 인플레이트합니다.
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AlbumRVAdapter.ViewHolder {
        val binding: ItemAlbumBinding = ItemAlbumBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup,false)

        return ViewHolder(binding)
    }

    //데이터를 추가하는 방법이다. Adapter 자체에서는 데이터의 변동을 인지할 수 없기에 notifyDataSetChanged 메소드를 사용한다.
    fun addItem(album: Album){
        albumList.add(album)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int){
        albumList.removeAt(position)
        notifyDataSetChanged()
    }

    // onBindViewHolder: 재사용할 수 있는 뷰를 데이터와 바인딩합니다. 주어진 위치의 데이터를 가져와서 ViewHolder의 뷰에 설정합니다.
    override fun onBindViewHolder(holder: AlbumRVAdapter.ViewHolder, position: Int) {
        holder.bind(albumList[position])
        holder.itemView.setOnClickListener{myItemClickListener.onItemClick(albumList[position])}
       // holder.binding.itemAlbumTitleTv.setOnClickListener { myItemClickListener.onRemoveAlbum(position) }
    }

    // getItemCount: 어댑터가 관리하는 데이터 세트의 크기를 반환합니다.
    override fun getItemCount(): Int {
        return albumList.size
    }

    // ViewHolder 클래스: 아이템 뷰를 보유하고 뷰의 초기화와 데이터 바인딩을 담당합니다.
    inner class ViewHolder(val binding: ItemAlbumBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(album:Album){
            binding.itemAlbumTitleTv.text = album.title
            binding.itemAlbumSingerTv.text = album.singer
            binding.itemAlbumCoverImgIv.setImageResource(album.coverImg!!)
        }
    }
}