package com.example.mobilepoject.messenger

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User(val uid: String, var username: String, var profileImageUrl: String?): Parcelable {
    constructor(): this("", "", "")
}