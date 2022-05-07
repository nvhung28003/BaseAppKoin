package com.example.baseappkoin.di

import com.example.baseappkoin.domain.respository.UserRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { UserRepository(get(), get()) }

}
