package com.example.baseappkoin.di


import com.example.baseappkoin.ui.introduce.splash.SplashViewModel
import com.example.baseappkoin.ui.notice.NoticeViewModel
import com.example.baseappkoin.ui.page.MyPageViewModel
import com.example.baseappkoin.ui.photo.PhotoViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { SplashViewModel(get()) }
    viewModel { PhotoViewModel(get()) }
    viewModel { NoticeViewModel(get()) }
    viewModel { MyPageViewModel(get(), get(), get()) }

}
