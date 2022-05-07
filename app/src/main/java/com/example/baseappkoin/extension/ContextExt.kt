package com.example.baseappkoin.extension

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.*
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.example.baseappkoin.utils.Constants.PERMISSION_CAMERA
import com.example.baseappkoin.utils.Constants.PERMISSION_READ_STORAGE
import com.example.baseappkoin.utils.Constants.PERMISSION_WRITE_STORAGE
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

fun Context.readDrawable(@DrawableRes resId: Int): Drawable {
    return drawable(resId)
}

fun Context.readText(@StringRes resId: Int, vararg formatArgs: Any): String {
    return getString(resId, *formatArgs)
}

fun Context.drawable(@DrawableRes resId: Int): Drawable {
    val drawable = ContextCompat.getDrawable(this, resId)
    return drawable ?: ColorDrawable()
}

fun Context.checkNetwork(): Flow<Boolean> = callbackFlow {
    val callback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            trySend(true).isSuccess
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            trySend(false)
        }
    }
    val networkManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    networkManager.registerNetworkCallback(
        NetworkRequest.Builder().addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build(),
        callback
    )
    awaitClose {
        networkManager.unregisterNetworkCallback(callback)
    }
}

fun Context.toast(resId: Int) = runOnUiThread {
    Toast.makeText(this, resId, Toast.LENGTH_SHORT).apply { show() }
}

fun Context.toast(text: CharSequence) = runOnUiThread {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).apply { show() }
}

fun Context.toastLong(resId: Int) = runOnUiThread {
    Toast.makeText(this, resId, Toast.LENGTH_LONG).apply { show() }
}

fun Context.toastLong(text: CharSequence) = runOnUiThread {
    Toast.makeText(this, text, Toast.LENGTH_LONG).apply { show() }
}

fun Context.hasPermission(permId: Int) = ContextCompat.checkSelfPermission(
    this,
    getPermissionString(permId)
) == PackageManager.PERMISSION_GRANTED

fun Context.getPermissionString(id: Int) = when (id) {
    PERMISSION_READ_STORAGE -> Manifest.permission.READ_EXTERNAL_STORAGE
    PERMISSION_WRITE_STORAGE -> Manifest.permission.WRITE_EXTERNAL_STORAGE
    PERMISSION_CAMERA -> Manifest.permission.CAMERA
    else -> ""
}

fun Context.checkExistUriFromStorage(uriLocal: Uri): Boolean {
    return try {
        this.contentResolver.openInputStream(uriLocal)?.close()
        true
    } catch (e: Exception) {
        false
    }
}

fun Context.openShare(text : String){
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(sendIntent, null)
    startActivity(shareIntent)
}

@Suppress("unused")
inline val Any?.unit
    get() = Unit