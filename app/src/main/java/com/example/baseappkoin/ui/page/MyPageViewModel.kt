package com.example.baseappkoin.ui.page

import android.app.Application
import com.example.baseappkoin.base.ui.BaseViewModel
import com.example.baseappkoin.data.user.model.User
import com.example.baseappkoin.domain.respository.UserRepository
import com.example.baseappkoin.utils.SharePreference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow


class MyPageViewModel(
    override val app: Application,
    private val sharePreference: SharePreference,
    private val userRepository: UserRepository,
) : BaseViewModel(app) {
    var user: User? = null

     val userInfoFlow: MutableSharedFlow<Boolean> = MutableSharedFlow(replay = 1)


    init {
        getUser()
    }

    fun getUser() {
        user = sharePreference.get<User>(SharePreference.KEY_USER)
    }

     fun getUserInfo() {
        jobCall = launchJob(Dispatchers.IO) {
            userRepository.getUserInfo()
                .collect {
                    if (it.isSuccess) {
                        sharePreference.apply {
                            save(SharePreference.KEY_USER, it.data?.user)
                        }
                        user =  it.data?.user
                        userInfoFlow.tryEmit(true)
                    }
                }
        }
    }
}