package kr.dori.android.umc_flomaking
//String, Int안에는 값을 하나만 담을 수 있기 때문에 dataclass를 사용하여 여러개의 파라미터를 이용하여 이를 해결한다.
//이미지 파일을 삽입하기 위한 파라미터의 데이터타입으로 Int형을 설정한다.
data class HomeList(val backgroundimg: Int, val title: String)