package com.example.baseappkoin.domain.remote.response

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.example.baseappkoin.base.ui.NetworkCode
import kotlinx.parcelize.Parcelize

@Parcelize
open class BaseResponse(
    open val code: Int,
    val message: String? = null
) : Parcelable {
    val isSuccess: Boolean
        get() = code == NetworkCode.SERVER_SUCCESS.code

    val messageNotNull: Boolean
        get() = message.isNullOrEmpty()
}


