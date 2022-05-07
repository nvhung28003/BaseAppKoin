package com.example.baseappkoin.customview.datetimepicker

import android.graphics.Typeface

interface IWheelPicker {
    fun setOnItemSelectedListener(listener: WheelPicker.OnItemSelectedListener?)

    fun getCurrentItemPosition(): Int

    fun getData(): List<String>

    fun setData(data: List<String>)

    fun setOnWheelChangeListener(listener: WheelPicker.OnWheelChangeListener?)

    fun getSelectedItemTextColor(): Int

    fun setSelectedItemTextColor(color: Int)

    fun getItemTextColor(): Int

    fun setItemTextColor(color: Int)

    fun getItemTextSize(): Int

    fun setItemTextSize(size: Int)

    fun getTypeface(): Typeface?

    fun setTypeface(tf: Typeface?)
}