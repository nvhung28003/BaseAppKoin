package com.example.baseappkoin.domain.respository

import com.example.baseappkoin.data.authorization.AuthorizationService
import com.example.baseappkoin.domain.remote.request.LoginRequest
import com.example.baseappkoin.domain.remote.response.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class UserRepository(
    private val apiAuthorizationService: AuthorizationService,

    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun loginAccount(type:Int,email:String?,name:String?,tokenFirebase:String): Flow<BaseResponse> =
        flow {
            val result = apiAuthorizationService.loginEmail(LoginRequest("email"))
            emit(result)
        }.flowOn(dispatcher)

}