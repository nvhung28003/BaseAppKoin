package com.example.baseappkoin.utils

import android.graphics.Typeface
import com.example.baseappkoin.BaseAppKoinApplication

/**
 * TypeFaceUtils。
 *
 * @author HoangChung by MacPro
 * @since  2022/2/21
 */
object TypeFaceUtil {

    const val BOLD_TYPEFACE = 1

    const val NORMAL_TYPEFACE = 2

    const val LIGHT_TYPEFACE = 3

    const val REGULAR_TYPEFACE = 4

    const val MEDIUM_TYPEFACE = 5

    const val HEAVY_TYPEFACE = 6

    private var boldTypeface: Typeface? = null

    private var normalTypeface: Typeface? = null

    private var regularTypeface: Typeface? = null

    private var mediumTypeface: Typeface? = null

    private var lightTypeface: Typeface? = null

    private var heavyTypeface: Typeface? = null

    fun getBoldTypeface() = if (boldTypeface == null) {
        try {
            boldTypeface = Typeface.createFromAsset(
                BaseAppKoinApplication.context.assets,
                "fonts/sourcehansans_bold.otf"
            )
            boldTypeface
        } catch (e: RuntimeException) {
            Typeface.DEFAULT
        }
    } else {
        boldTypeface!!
    }

    fun getNormalTypeface() = if (normalTypeface == null) {
        try {
            normalTypeface = Typeface.createFromAsset(
                BaseAppKoinApplication.context.assets,
                "fonts/sourcehansans_normal.otf"
            )
            normalTypeface
        } catch (e: RuntimeException) {
            Typeface.DEFAULT
        }
    } else {
        normalTypeface!!
    }

    fun getRegularTypeface() = if (regularTypeface == null) {
        try {
            regularTypeface = Typeface.createFromAsset(
                BaseAppKoinApplication.context.assets,
                "fonts/sourcehansans_regular.otf"
            )
            regularTypeface
        } catch (e: RuntimeException) {
            Typeface.DEFAULT
        }
    } else {
        regularTypeface!!
    }

    fun getMediumTypeface() = if (mediumTypeface == null) {
        try {
            mediumTypeface = Typeface.createFromAsset(
                BaseAppKoinApplication.context.assets,
                "fonts/sourcehansans_medium.otf"
            )
            mediumTypeface
        } catch (e: RuntimeException) {
            Typeface.DEFAULT
        }

    } else {
        mediumTypeface!!
    }

    fun getLightTypeface() = if (lightTypeface == null) {
        try {
            lightTypeface = Typeface.createFromAsset(
                BaseAppKoinApplication.context.assets,
                "fonts/sourcehansans_light.otf"
            )
            lightTypeface
        } catch (e: RuntimeException) {
            Typeface.DEFAULT
        }
    } else {
        lightTypeface!!
    }

    fun getHeavyTypeface() = if (heavyTypeface == null) {
        try {
            heavyTypeface = Typeface.createFromAsset(
                BaseAppKoinApplication.context.assets,
                "fonts/sourcehansans_heavy.otf"
            )
            heavyTypeface
        } catch (e: RuntimeException) {
            Typeface.DEFAULT
        }
    } else {
        heavyTypeface!!
    }

}