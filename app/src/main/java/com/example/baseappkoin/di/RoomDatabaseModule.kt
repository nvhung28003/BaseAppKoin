package com.example.baseappkoin.di

import android.content.Context
import androidx.room.Room
import com.example.baseappkoin.utils.SharePreference
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val roomDatabaseModule = module {
//    single {
//        Room.databaseBuilder(
//            androidContext(), AppDatabase::class.java, "appdatabase.db"
//        )
//            .fallbackToDestructiveMigration()
//            .build()
//    }
}