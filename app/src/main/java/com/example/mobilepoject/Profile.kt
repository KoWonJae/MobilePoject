package com.example.mobilepoject

import java.io.Serializable

class Profile(var name:String, var phoneNumber:Int, var grade:String , var record:String, var tag:String) : Serializable {
    constructor():this("noinfo", 0, "noinfo", "noinfo", "noinfo")
}