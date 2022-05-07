package com.example.baseappkoin.extension

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import timber.log.Timber

inline fun <T> Flow<T>.collectIn(
  owner: LifecycleOwner,
  minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
  crossinline action: suspend (value: T) -> Unit,
): Job = owner.lifecycleScope.launch {
  owner.lifecycle.repeatOnLifecycle(state = minActiveState) {
    Timber.d("Start collecting...")
    collect { action(it) }
  }
}

inline fun <T> Flow<T>.collectIn(
  fragment: Fragment,
  minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
  crossinline action: suspend (value: T) -> Unit,
): Job = fragment.lifecycleScope.launch {
  fragment.viewLifecycleOwner.repeatOnLifecycle(state = minActiveState) {
    Timber.d("Start collecting...")
    collect { action(it) }
  }
}
