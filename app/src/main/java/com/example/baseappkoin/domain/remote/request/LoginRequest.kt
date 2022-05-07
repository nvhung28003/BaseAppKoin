package com.example.baseappkoin.domain.remote.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class LoginRequest (
    @SerializedName("email")
    val os: String,
): Parcelable