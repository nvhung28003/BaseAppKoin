package com.example.baseappkoin.extension

import androidx.annotation.CheckResult
import androidx.viewpager2.widget.ViewPager2
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate

/**
 * Convert Viewpager2 to Flow ... Nice <3
 */
@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
fun ViewPager2.pageSelections(): InitialValueFlow<Int> = callbackFlow {
    checkMainThread()
    val callback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            trySend(position)
        }
    }
    registerOnPageChangeCallback(callback)
    awaitClose { unregisterOnPageChangeCallback(callback) }
}
    .conflate()
    .asInitialValueFlow { currentItem }

@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
fun ViewPager2.pageScrollStateChanges(): Flow<Int> = callbackFlow {
    checkMainThread()
    val callback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrollStateChanged(state: Int) {
            trySend(state)
        }
    }
    registerOnPageChangeCallback(callback)
    awaitClose { unregisterOnPageChangeCallback(callback) }
}.conflate()

@CheckResult
@OptIn(ExperimentalCoroutinesApi::class)
fun ViewPager2.pageScrollEvents(): Flow<ViewPager2PageScrollEvent> = callbackFlow {
    checkMainThread()
    val callback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            trySend(
                ViewPager2PageScrollEvent(
                    view = this@pageScrollEvents,
                    position = position,
                    positionOffset = positionOffset,
                    positionOffsetPixel = positionOffsetPixels
                )
            )
        }
    }
    registerOnPageChangeCallback(callback)
    awaitClose { unregisterOnPageChangeCallback(callback) }
}.conflate()

data class ViewPager2PageScrollEvent(
    val view: ViewPager2,
    val position: Int,
    val positionOffset: Float,
    val positionOffsetPixel: Int
)

internal val ViewPager2.isIdle get() = scrollState == ViewPager2.SCROLL_STATE_IDLE