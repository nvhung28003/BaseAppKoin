package com.example.baseappkoin.customview

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatRadioButton
import com.example.baseappkoin.R

import com.example.baseappkoin.utils.TypeFaceUtil


class TypeFaceRadioButton : AppCompatRadioButton {


    constructor(context: Context) : super(context){
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet?) :
            super(context, attrs){
        init(attrs)
    }


    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.TypeFaceRadioButton, 0, 0)
            val typefaceType = typedArray.getInt(R.styleable.TypeFaceRadioButton_fontSelectRadio, 0)
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