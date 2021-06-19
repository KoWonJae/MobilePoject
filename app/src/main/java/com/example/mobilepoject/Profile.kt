package com.example.mobilepoject

import java.io.Serializable

class Profile(var name:String, var phoneNumber:String, var email:String , var selfinfo:String, var tag:String) : Serializable {
    constructor():this("noinfo", "noinfo", "noinfo", "noinfo", "noinfo")

}