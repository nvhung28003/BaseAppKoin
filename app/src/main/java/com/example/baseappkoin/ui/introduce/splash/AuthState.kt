package com.example.baseappkoin.ui.introduce.splash

sealed class AuthState {
    object Available : AuthState()
    object Valid : AuthState()
    object NotLogin : AuthState()
    object LoggedNotRegisterType : AuthState()
}
