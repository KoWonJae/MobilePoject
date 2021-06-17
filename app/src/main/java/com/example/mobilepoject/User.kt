package com.example.mobilepoject

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

//@Parcelize
//class User(val uid: String, var username: String, var profileImageUrl: String?): Parcelable {
//    constructor(): this("", "", "")
//}
@Parcelize
class User(var uid:String, var username:String, var phoneNumber:String, var email:String , var selfinfo:String, var tag:String, var profileImageUrl: String, var site: String, var career:String): Parcelable {
    constructor():this("", "", "", "", "", "", "", "", "")
}