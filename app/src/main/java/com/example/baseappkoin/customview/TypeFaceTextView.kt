package com.example.baseappkoin.customview

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.example.baseappkoin.R
import com.example.baseappkoin.utils.TypeFaceUtil

/**
 * TextView。
 *
 * @author HoangChung by MacPro
 * @since  2022/2/21
 */
class TypefaceTextView : AppCompatTextView {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.TypefaceTextView, 0, 0)
            val typefaceType = typedArray.getInt(R.styleable.TypefaceTextView_fontSelect, 0)
            typeface = getTypeface(typefaceType)
            includeFontPadding = false
            typedArray.recycle()
        }
    }

    companion object {
        fun getTypeface(typefaceType: Int?) = when (typefaceType) {
            TypeFaceUtil.BOLD_TYPEFACE -> TypeFaceUtil.getBoldTypeface()
            TypeFaceUtil.NORMAL_TYPEFACE -> TypeFaceUtil.getNormalTypeface()
            TypeFaceUtil.LIGHT_TYPEFACE -> TypeFaceUtil.getLightTypeface()
            TypeFaceUtil.MEDIUM_TYPEFACE -> TypeFaceUtil.getMediumTypeface()
            TypeFaceUtil.REGULAR_TYPEFACE -> TypeFaceUtil.getRegularTypeface()
            TypeFaceUtil.HEAVY_TYPEFACE -> TypeFaceUtil.getHeavyTypeface()
            else -> Typeface.DEFAULT
        }
    }
}