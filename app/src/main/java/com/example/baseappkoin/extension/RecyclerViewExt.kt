package com.example.baseappkoin.extension

import android.view.View
import androidx.annotation.CheckResult
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import timber.log.Timber

/**
 * Convert RecyclerView to Flow ... Nice <3
 */
@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
fun RecyclerView.loadMoreFlow(): Flow<Int> = callbackFlow {
    checkMainThread()
    val listener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (!recyclerView.canScrollVertically(1)) {
                trySend(newState)
                Timber.d("LoadMore enable >>>> ")
            }

        }
    }
    addOnScrollListener(listener)
    awaitClose { removeOnScrollListener(listener) }
}.conflate()

@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
fun RecyclerView.scrollEvents(): Flow<RecyclerViewScrollEvent> = callbackFlow {
    checkMainThread()
    val listener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            trySend(
                RecyclerViewScrollEvent(
                    view = this@scrollEvents,
                    dx = dx,
                    dy = dy
                )
            )
        }
    }
    addOnScrollListener(listener)
    awaitClose { removeOnScrollListener(listener) }
}.conflate()

data class RecyclerViewScrollEvent(
    public val view: RecyclerView,
    public val dx: Int,
    public val dy: Int
)

