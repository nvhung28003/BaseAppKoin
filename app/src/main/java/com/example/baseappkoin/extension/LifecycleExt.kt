package com.example.baseappkoin.extension

import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

// For collect many flow state
// Follow lifecycle
fun Fragment.launchAndRepeatStarted(
    vararg launchBlock: suspend () -> Unit,
    doAfterLaunch: (() -> Unit)? = null
) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            launchBlock.forEach {
                launch { it.invoke() }
            }
            doAfterLaunch?.invoke()
        }
    }
}
