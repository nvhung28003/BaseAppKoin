package com.example.baseappkoin.domain.remote.interceptor

import com.example.baseappkoin.utils.SharePreference
import com.example.baseappkoin.utils.SharePreference.Companion.KEY_ID_TOKEN

class AccessTokenProvider(private val sharedPreference: SharePreference) {
    val accessToken: String?
        get() = sharedPreference.get(KEY_ID_TOKEN)
    val isAuthorized: Boolean
        get() = !accessToken.isNullOrEmpty()
}
