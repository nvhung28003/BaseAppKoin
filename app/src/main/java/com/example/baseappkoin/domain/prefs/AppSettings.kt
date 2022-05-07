package com.example.baseappkoin.domain.prefs

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow

class AppSettings(context: Context) {

    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)



    fun observe() = callbackFlow<String> {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            trySendBlocking(key)
        }
        prefs.registerOnSharedPreferenceChangeListener(listener)
        awaitClose {
            prefs.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }

    fun subscribe(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        prefs.registerOnSharedPreferenceChangeListener(listener)
    }

    fun unsubscribe(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        prefs.unregisterOnSharedPreferenceChangeListener(listener)
    }

}