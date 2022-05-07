package com.example.baseappkoin.data.authorization

import com.example.baseappkoin.domain.remote.request.LoginRequest
import com.example.baseappkoin.domain.remote.response.BaseResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthorizationService {
    @POST("loginEmail")
    suspend fun loginEmail(
        @Body body: LoginRequest
    ): BaseResponse

}