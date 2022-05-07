package com.example.baseappkoin.ui.introduce.splash

import android.app.Application
import com.example.baseappkoin.base.ui.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow

class SplashViewModel(
    override val app: Application)
    : BaseViewModel(app) {
    val stateFlow: MutableStateFlow<AuthState> = MutableStateFlow(AuthState.Valid)


    init {
        jobCall = launchJob {
            delay(1000)
            stateFlow.value = AuthState.Available
        }
    }
}