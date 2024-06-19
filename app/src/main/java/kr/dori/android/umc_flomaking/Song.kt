package kr.dori.android.umc_flomaking

data class Song( //Seekbar 작업을 위해서 item 추가
    val title:String = "",
    val singer:String = "",
    val second:Int = 0, //진행 시간
    val playTime:Int = 0, //총 시간
    var isPlaying:Boolean = false
)
