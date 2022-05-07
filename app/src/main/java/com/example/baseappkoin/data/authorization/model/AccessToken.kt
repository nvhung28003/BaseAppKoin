package com.example.baseappkoin.data.authorization.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AccessToken(
    val access_token: String?,
    val refresh_token: String?,
    val token_type: String?= null,
    val scope: String? = null ,
    val create_at: Int?= null
)
