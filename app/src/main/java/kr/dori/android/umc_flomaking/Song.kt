package kr.dori.android.umc_flomaking

data class Song( //Seekbar 작업을 위해서 item 추가
    val title:String = "",
    val singer:String = "",
    var second:Int = 0, //진행 시간
    var playTime:Int = 0, //총 시간
    var isPlaying:Boolean = false,
    var music:String = ""
)
