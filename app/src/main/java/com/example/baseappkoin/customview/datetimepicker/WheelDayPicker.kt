package com.example.baseappkoin.customview.datetimepicker

import android.content.Context
import android.util.AttributeSet
import com.example.baseappkoin.R
import java.util.*

class WheelDayPicker @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    WheelPicker(context, attrs) {
    private val mCalendar: Calendar = Calendar.getInstance()
    private var year: Int
    private var month: Int
    private var selectedDay: Int

    private fun updateDays() {
        mCalendar[Calendar.YEAR] = year
        mCalendar[Calendar.MONTH] = month
        val days = mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val data = mutableListOf<String>()
        for (i in 1..days) data.add(i.toString() + context.getString(R.string.title_day))
        DAYS[days] = data
        super.setData(data)
    }

    private fun updateSelectedDay() {
        setSelectedItemPosition(selectedDay - 1, true)
    }

    override fun setData(data: List<String>) {
        throw UnsupportedOperationException("You can not invoke setData in WheelDayPicker")
    }

    fun getSelectedDay(): Int {
        return selectedDay
    }

    fun setSelectedDay(day: Int) {
        selectedDay = day
        updateSelectedDay()
    }

    fun setYearAndMonth(year: Int, month: Int) {
        this.year = year
        this.month = month - 1
        updateDays()
    }

    fun getYear(): Int {
        return year
    }

    fun setYear(year: Int) {
        this.year = year
        updateDays()
    }

    fun getMonth(): Int {
        return month
    }

    fun setMonth(month: Int) {
        this.month = month - 1
        updateDays()
    }

    override fun run() {
        super.run()
        selectedDay = getData()[getCurrentItemPosition()].filter { it.isDigit() }.toInt()
    }

    companion object {
        private val DAYS: MutableMap<Int, MutableList<String>> = HashMap()
    }

    init {
        year = mCalendar[Calendar.YEAR]
        month = mCalendar[Calendar.MONTH]
        updateDays()
        selectedDay = mCalendar[Calendar.DAY_OF_MONTH]
        updateSelectedDay()
    }
}