package com.example.baseappkoin.customview.datetimepicker

import android.content.Context
import android.util.AttributeSet
import com.example.baseappkoin.R
import java.util.*
import java.util.Calendar.YEAR

class WheelYearPicker @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    WheelPicker(context, attrs) {
    val calendar = Calendar.getInstance()
    private var mYearStart = 1900
    private var mYearEnd = calendar.get(YEAR) + 100
    private var selectedYear: Int
    private fun updateYears() {
        val data: MutableList<String> = ArrayList()
        for (i in mYearStart..mYearEnd) data.add(
            i.toString() + context.getString(
                R.string.title_year
            )
        )
        super.setData(data)
    }

    private fun updateSelectedYear() {
        setSelectedItemPosition(selectedYear - mYearStart, true)
    }

    override fun setData(data: List<String>) {
        throw UnsupportedOperationException("You can not invoke setData in WheelYearPicker")
    }

    fun setYearFrame(start: Int, end: Int) {
        mYearStart = start
        mYearEnd = end
        selectedYear = calendar[YEAR]
        updateYears()
        updateSelectedYear()
    }

    fun getYearStart(): Int {
        return mYearStart
    }

    fun setYearStart(start: Int) {
        mYearStart = start
        selectedYear = calendar[YEAR]
        updateYears()
        updateSelectedYear()
    }

    fun getYearEnd(): Int {
        return mYearEnd
    }

    fun setYearEnd(end: Int) {
        mYearEnd = end
        updateYears()
    }

    fun getSelectedYear(): Int {
        return selectedYear
    }

    fun setSelectedYear(year: Int) {
        selectedYear = year
        updateSelectedYear()
    }

    override fun run() {
        super.run()
        selectedYear = getData()[getCurrentItemPosition()].filter { it.isDigit() }.toInt()
    }

    init {
        updateYears()
        selectedYear = Calendar.getInstance()[YEAR]
        updateSelectedYear()
    }
}