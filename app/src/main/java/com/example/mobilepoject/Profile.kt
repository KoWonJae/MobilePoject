package com.example.mobilepoject

import java.io.Serializable

class Profile(var name:String, var phoneNumber:Int, var email:String , var selfinfo:String, var tag:String) : Serializable {
    constructor():this("noinfo", 0, "noinfo", "noinfo", "noinfo")
    //사진, 자기소개 , URL정보 grade빼기, record 나중에추가
    // 이름 번호 이메일 자기소개 태그 레코드
}