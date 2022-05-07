package com.example.baseappkoin.domain.remote.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

class AccessTokenInterceptor(private val accessTokenProvider: AccessTokenProvider) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var builder = request.newBuilder()
        Timber.d("TokenPresent >>>> ${accessTokenProvider.accessToken}")
        val shouldAddAuthHeaders = request.headers["includeAuthorizable"] != "false"
        builder = builder.removeHeader("includeAuthorizable")
        if (shouldAddAuthHeaders && accessTokenProvider.isAuthorized) {
            builder = builder
                .addHeader("Authorization", "Bearer ${accessTokenProvider.accessToken}")
            val response = chain.proceed(builder.build())
            // TODO handle refresh token
            return response
        }
        return chain.proceed(builder.build())
    }
}
