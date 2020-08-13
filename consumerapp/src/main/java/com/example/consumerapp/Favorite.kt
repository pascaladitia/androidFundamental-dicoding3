package com.example.consumerapp

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Favorite (
    var id: Int = 0,
    var login: String? = null,
    var avatar_url: String? = null
) : Parcelable