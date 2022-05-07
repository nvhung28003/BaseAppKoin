package com.example.baseappkoin.customview

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import com.example.baseappkoin.R
import com.google.android.material.textfield.TextInputEditText
import com.example.baseappkoin.utils.TypeFaceUtil


class TypeFaceEditText : TextInputEditText {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs,androidx.appcompat.R.attr.editTextStyle)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int = androidx.appcompat.R.attr.editTextStyle) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.TypeFaceEditText,
                defStyleAttr, 0)
            val typefaceType = typedArray.getInt(R.styleable.TypeFaceEditText_fontSelectEditText, 0)
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
            else -> TypeFaceUtil.getRegularTypeface()
        }
    }
}