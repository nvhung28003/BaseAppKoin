package com.example.baseappkoin.extension

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.coroutineScope

import timber.log.Timber
import java.io.File


inline fun <T : Fragment> T.withArgs(size: Int, block: Bundle.() -> Unit): T {
    val b = Bundle(size)
    b.block()
    this.arguments = b
    return this
}

val Fragment.viewLifecycleScope
    inline get() = viewLifecycleOwner.lifecycle.coroutineScope

@Suppress("NOTHING_TO_INLINE")
inline fun <T : Parcelable> Fragment.parcelableArgument(name: String): Lazy<T> {
    return lazy(LazyThreadSafetyMode.NONE) {
        requireNotNull(arguments?.getParcelable(name)) {
            "No argument $name passed into ${javaClass.simpleName}"
        }
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun Fragment.stringArgument(name: String) = lazy(LazyThreadSafetyMode.NONE) {
    arguments?.getString(name)
}
inline fun Fragment.booleanArgument(name: String) = lazy(LazyThreadSafetyMode.NONE) {
    arguments?.getBoolean(name)
}
@Suppress("unused")
fun Fragment.toast(resId: Int) = requireContext().toast(resId)

fun Fragment.toast(text: CharSequence) = requireContext().toast(text)

@Suppress("unused")
fun Fragment.disableUi() {
    activity?.window?.setFlags(
        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
    )
    Timber.d("UI disabled.")
}

@Suppress("unused")
fun Fragment.enableUi() {
    activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    Timber.d("UI enabled.")
}

fun Fragment.getLaunchIntent(name: String): Intent? {
    return requireActivity().packageManager.getLaunchIntentForPackage(name)
}


 fun Fragment.deleteCache() {
    try {
        val dir: File = requireContext().cacheDir
        deleteDir(dir)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

private fun deleteDir(dir: File?): Boolean {
    return if (dir != null && dir.isDirectory()) {
        val children: Array<String> = dir.list()
        for (i in children.indices) {
            val success = deleteDir(File(dir, children[i]))
            if (!success) {
                return false
            }
        }
        dir.delete()
    } else if (dir != null && dir.isFile) {
        dir.delete()
    } else {
        false
    }
}



fun Fragment.requireAppContext(): Context = requireContext().applicationContext



