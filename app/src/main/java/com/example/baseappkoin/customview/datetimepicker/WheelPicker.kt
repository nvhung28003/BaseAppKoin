package com.example.baseappkoin.customview.datetimepicker

import android.content.Context
import android.graphics.*
import android.os.Handler
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import android.widget.Scroller
import com.example.baseappkoin.R

open class WheelPicker constructor(context: Context, attrs: AttributeSet? = null) :
    View(context, attrs), IWheelPicker, Runnable {
    private val newHandler = Handler()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG or Paint.LINEAR_TEXT_FLAG)
    private val scroller: Scroller
    private var tracker: VelocityTracker? = null
    private var isTouchTriggered = false
    private var onItemSelectedListener: OnItemSelectedListener? = null
    private var onWheelChangeListener: OnWheelChangeListener? = null
    private val rectDrawn: Rect
    private val rectCurrentItem: Rect
    private val camera: Camera
    private val matrixRotate: Matrix
    private val matrixDepth: Matrix
    private var data: List<String> = listOf()
    private var drawnItemCount = 0
    private var halfDrawnItemCount = 0
    private var textMaxWidth = 0
    private var textMaxHeight = 0
    private var itemTextColor: Int
    private var selectedItemTextColor: Int
    private var itemTextSize: Int
    private var itemHeight = 0
    private var halfItemHeight = 0
    private var halfWheelHeight = 0
    private var currentItemPosition = 0
    private var selectedItemPosition = 0
    private var minFlingY = 0
    private var maxFlingY = 0
    private var minimumVelocity = 50
    private var maximumVelocity = 8000
    private var wheelCenterX = 0
    private var wheelCenterY = 0
    private var drawnCenterX = 0
    private var drawnCenterY = 0
    private var scrollOffsetY = 0
    private var lastPointY = 0
    private var downPointY = 0
    private var touchSlop = 8
    private var visibleItemCount = 7

    private var isClick = false
    private val isCyclic = false
    private var isForceFinishScroll = false

    /**
     * Font typeface path from assets
     */
    private val fontPath: String?
    private val isDebug = false
    private fun updateVisibleItemCount() {
        if (visibleItemCount % 2 == 0)
            visibleItemCount += 1
        drawnItemCount = visibleItemCount + 2
        halfDrawnItemCount = drawnItemCount / 2
    }

    private fun computeTextSize() {
        textMaxHeight = 0
        textMaxWidth = textMaxHeight
        data.forEach {
            val text = it.toString()
            val width = paint.measureText(text).toInt()
            textMaxWidth = textMaxWidth.coerceAtLeast(width)
        }
        val metrics = paint.fontMetrics
        textMaxHeight = (metrics.bottom - metrics.top).toInt()
    }

    private fun updateItemTextAlign() {
        paint.textAlign = Paint.Align.CENTER
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val modeWidth = MeasureSpec.getMode(widthMeasureSpec)
        val modeHeight = MeasureSpec.getMode(heightMeasureSpec)
        val sizeWidth = MeasureSpec.getSize(widthMeasureSpec)
        val sizeHeight = MeasureSpec.getSize(heightMeasureSpec)

        // Correct sizes of original content
        var resultWidth = textMaxWidth
        var resultHeight =
            textMaxHeight * visibleItemCount + resources.getDimensionPixelSize(R.dimen.dp_12) * (visibleItemCount - 1)

        // Correct view sizes again if curved is enable
        resultHeight = (2 * resultHeight / Math.PI).toInt()

        // Consideration padding influence the view sizes
        resultWidth += paddingLeft + paddingRight
        resultHeight += paddingTop + paddingBottom

        // Consideration sizes of parent can influence the view sizes
        resultWidth = measureSize(modeWidth, sizeWidth, resultWidth)
        resultHeight = measureSize(modeHeight, sizeHeight, resultHeight)
        setMeasuredDimension(resultWidth, resultHeight)
    }

    private fun measureSize(mode: Int, sizeExpect: Int, sizeActual: Int): Int {
        var realSize: Int
        if (mode == MeasureSpec.EXACTLY) {
            realSize = sizeExpect
        } else {
            realSize = sizeActual
            if (mode == MeasureSpec.AT_MOST) realSize = Math.min(realSize, sizeExpect)
        }
        return realSize
    }

    override fun onSizeChanged(w: Int, h: Int, oldW: Int, oldH: Int) {
        rectDrawn[paddingLeft, paddingTop, width - paddingRight] = height - paddingBottom

        wheelCenterX = rectDrawn.centerX()
        wheelCenterY = rectDrawn.centerY()

        // 计算数据项绘制中心
        // Correct item drawn center
        computeDrawnCenter()
        halfWheelHeight = rectDrawn.height() / 2
        itemHeight = rectDrawn.height() / visibleItemCount
        halfItemHeight = itemHeight / 2

        // 初始化滑动最大坐标
        // Initialize fling max Y-coordinates
        computeFlingLimitY()

        // 计算当前选中的数据项区域
        // Correct region of current select item
        computeCurrentItemRect()
    }

    private fun computeDrawnCenter() {
        drawnCenterX = wheelCenterX
        if (paint == null) {
            return
        }
        drawnCenterY = (wheelCenterY - (paint.ascent() + paint.descent()) / 2).toInt()
    }

    private fun computeFlingLimitY() {
        val currentItemOffset: Int = selectedItemPosition * itemHeight
        minFlingY = -itemHeight * (data.size - 1) + currentItemOffset
        maxFlingY = currentItemOffset
    }

    private fun computeCurrentItemRect() {
        if (selectedItemTextColor == -1) return
        rectCurrentItem[rectDrawn.left, wheelCenterY - halfItemHeight, rectDrawn.right] =
            wheelCenterY + halfItemHeight
    }

    fun setVisibleItemCount(visibleItemCount: Int) {
        this.visibleItemCount = visibleItemCount
        updateVisibleItemCount()
        requestLayout()
    }

    fun getVisibleItemCount() = visibleItemCount

    override fun onDraw(canvas: Canvas) {
        if (null != onWheelChangeListener) onWheelChangeListener?.onWheelScrolled(scrollOffsetY)
        if (data.isEmpty()) return
        val drawnDataStartPos = -scrollOffsetY / itemHeight - halfDrawnItemCount
        var drawnDataPos = drawnDataStartPos + selectedItemPosition
        var drawnOffsetPos = -halfDrawnItemCount
        while (drawnDataPos < drawnDataStartPos + selectedItemPosition + drawnItemCount) {
            var data = ""
            if (isPosInRang(drawnDataPos)) {
                data = this.data[drawnDataPos]
            }
            paint.color = itemTextColor
            paint.style = Paint.Style.FILL
            val mDrawnItemCenterY =
                drawnCenterY + drawnOffsetPos * itemHeight + scrollOffsetY % itemHeight
            var distanceToCenter: Int
            // 计算数据项绘制中心距离滚轮中心的距离比率
            // Correct ratio of item's drawn center to wheel center
            val ratio = (drawnCenterY - Math.abs(drawnCenterY - mDrawnItemCenterY) -
                    rectDrawn.top) * 1.0f / (drawnCenterY - rectDrawn.top)

            // 计算单位
            // Correct unit
            var unit = 0
            if (mDrawnItemCenterY > drawnCenterY) unit =
                1 else if (mDrawnItemCenterY < drawnCenterY) unit = -1
            var degree = -(1 - ratio) * 90 * unit
            if (degree < -90) degree = -90f
            if (degree > 90) degree = 90f
            distanceToCenter = computeSpace(degree.toInt())
            var transX = wheelCenterX
            val transY = wheelCenterY - distanceToCenter
            camera.save()
            camera.rotateX(degree)
            camera.getMatrix(matrixRotate)
            camera.restore()
            matrixRotate.preTranslate(-transX.toFloat(), -transY.toFloat())
            matrixRotate.postTranslate(transX.toFloat(), transY.toFloat())
            camera.save()
            camera.translate(0f, 0f, computeDepth(45).toFloat())
            camera.getMatrix(matrixDepth)
            camera.restore()
            matrixDepth.preTranslate(-transX.toFloat(), -transY.toFloat())
            matrixDepth.postTranslate(transX.toFloat(), transY.toFloat())
            matrixRotate.postConcat(matrixDepth)
            // Correct item's drawn centerY base on curved state
            val drawnCenterY = drawnCenterY - distanceToCenter

            // Judges need to draw different color for current item or not
            if (selectedItemTextColor != -1) {
                canvas.save()
                canvas.concat(matrixRotate)
                canvas.clipRect(rectCurrentItem, Region.Op.DIFFERENCE)
                canvas.drawText(data, drawnCenterX.toFloat(), drawnCenterY.toFloat(), paint)
                canvas.restore()
                paint.color = selectedItemTextColor
                canvas.save()
                canvas.concat(matrixRotate)
                canvas.clipRect(rectCurrentItem)
                canvas.drawText(data, drawnCenterX.toFloat(), drawnCenterY.toFloat(), paint)
                canvas.restore()
            } else {
                canvas.save()
                canvas.clipRect(rectDrawn)
                canvas.concat(matrixRotate)
                canvas.drawText(data, drawnCenterX.toFloat(), drawnCenterY.toFloat(), paint)
                canvas.restore()
            }
            drawnDataPos++
            drawnOffsetPos++
        }

        canvas.drawRect(
            0f,
            (height - paddingBottom).toFloat(),
            width.toFloat(),
            height.toFloat(),
            paint
        )
    }

    private fun computeSpace(degree: Int): Int {
        return (Math.sin(Math.toRadians(degree.toDouble())) * halfWheelHeight).toInt()
    }

    private fun isPosInRang(position: Int): Boolean {
        return position >= 0 && position < data.size
    }

    private fun computeDepth(degree: Int): Int {
        return (halfWheelHeight - Math.cos(Math.toRadians(degree.toDouble())) * halfWheelHeight).toInt()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                isTouchTriggered = true
                if (null != parent) parent.requestDisallowInterceptTouchEvent(true)
                if (tracker == null) tracker = VelocityTracker.obtain() else tracker?.clear()
                tracker?.addMovement(event)
                if (!scroller.isFinished) {
                    scroller.abortAnimation()
                    isForceFinishScroll = true
                }
                run {
                    lastPointY = event.y.toInt()
                    downPointY = lastPointY
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (Math.abs(downPointY - event.y) < touchSlop) {
                    isClick = true
                    return true
                }
                isClick = false
                tracker?.addMovement(event)
                if (onWheelChangeListener != null) onWheelChangeListener?.onWheelScrollStateChanged(
                    SCROLL_STATE_DRAGGING
                )

                // 滚动内容
                // Scroll WheelPicker's content
                val move = event.y - lastPointY
                if (Math.abs(move) < 1) return true
                scrollOffsetY += move.toInt()
                lastPointY = event.y.toInt()
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                if (null != parent) parent.requestDisallowInterceptTouchEvent(false)
                if (isClick && !isForceFinishScroll) return true
                tracker?.addMovement(event)
                tracker?.computeCurrentVelocity(1000, maximumVelocity.toFloat())

                // 根据速度判断是该滚动还是滑动
                // Judges the WheelPicker is scroll or fling base on current velocity
                isForceFinishScroll = false
                val velocity = tracker?.yVelocity?.toInt()
                if (velocity != null) {
                    if (Math.abs(velocity) > minimumVelocity) {
                        scroller.fling(0, scrollOffsetY, 0, velocity, 0, 0, minFlingY, maxFlingY)
                        scroller.finalY = scroller.finalY +
                                computeDistanceToEndPoint(scroller.finalY % itemHeight)
                    } else {
                        scroller.startScroll(
                            0, scrollOffsetY, 0,
                            computeDistanceToEndPoint(scrollOffsetY % itemHeight)
                        )
                    }
                }
                if (!isCyclic)
                    if (scroller.finalY > maxFlingY)
                        scroller.finalY = maxFlingY
                    else if (scroller.finalY < minFlingY)
                        scroller.finalY = minFlingY
                newHandler.post(this)
                if (null != tracker) {
                    tracker?.recycle()
                    tracker = null
                }
            }
            MotionEvent.ACTION_CANCEL -> {
                if (null != parent) parent.requestDisallowInterceptTouchEvent(false)
                if (null != tracker) {
                    tracker?.recycle()
                    tracker = null
                }
            }
        }
        return true
    }

    private fun computeDistanceToEndPoint(remainder: Int): Int {
        return if (Math.abs(remainder) > halfItemHeight) if (scrollOffsetY < 0) -itemHeight - remainder else itemHeight - remainder else -remainder
    }

    override fun run() {
        if (data.isEmpty()) return
        if (scroller.isFinished && !isForceFinishScroll) {
            if (itemHeight == 0) return
            var position = (-scrollOffsetY / itemHeight + selectedItemPosition) % data.size
            position = if (position < 0) position + data.size else position
            currentItemPosition = position
            if (null != onItemSelectedListener && isTouchTriggered) onItemSelectedListener?.onItemSelected(
                this,
                data[position],
                position
            )
            if (onWheelChangeListener != null && isTouchTriggered) {
                onWheelChangeListener?.onWheelSelected(position)
                onWheelChangeListener?.onWheelScrollStateChanged(SCROLL_STATE_IDLE)
            }
        }
        if (scroller.computeScrollOffset()) {
            if (onWheelChangeListener != null) onWheelChangeListener?.onWheelScrollStateChanged(
                SCROLL_STATE_SCROLLING
            )
            scrollOffsetY = scroller.currY
            postInvalidate()
            newHandler.postDelayed(this, 16)
        }
    }

    override fun setOnItemSelectedListener(listener: OnItemSelectedListener?) {
        onItemSelectedListener = listener
    }


    fun setSelectedItemPosition(position: Int, animated: Boolean) {
        isTouchTriggered = false
        if (animated && scroller.isFinished) { // We go non-animated regardless of "animated" parameter if scroller is in motion
            val length = getData().size
            var itemDifference: Int = position - currentItemPosition
            if (itemDifference == 0) return
            if (isCyclic && Math.abs(itemDifference) > length / 2) { // Find the shortest path if it's cyclic
                itemDifference += if (itemDifference > 0) -length else length
            }
            scroller.startScroll(0, scroller.currY, 0, -itemDifference * itemHeight)
            newHandler.post(this)
        } else {
            if (!scroller.isFinished) scroller.abortAnimation()
            var currentPosition = position
            currentPosition = Math.min(currentPosition, data.size - 1)
            currentPosition = Math.max(currentPosition, 0)
            selectedItemPosition = currentPosition
            currentItemPosition = currentPosition
            scrollOffsetY = 0
            computeFlingLimitY()
            requestLayout()
            invalidate()
        }
    }

    override fun getCurrentItemPosition(): Int {
        return currentItemPosition
    }

    override fun getData(): List<String> {
        return data
    }

    override fun setData(data: List<String>) {
        this.data = data

        if (selectedItemPosition > data.size - 1 || currentItemPosition > data.size - 1) {
            currentItemPosition = data.size - 1
            selectedItemPosition = currentItemPosition
        } else {
            selectedItemPosition = currentItemPosition
        }
        scrollOffsetY = 0
        computeTextSize()
        computeFlingLimitY()
        requestLayout()
        invalidate()
    }

    override fun setOnWheelChangeListener(listener: OnWheelChangeListener?) {
        onWheelChangeListener = listener
    }

    override fun getSelectedItemTextColor(): Int {
        return selectedItemTextColor
    }

    override fun setSelectedItemTextColor(color: Int) {
        selectedItemTextColor = color
        computeCurrentItemRect()
        invalidate()
    }

    override fun getItemTextColor(): Int {
        return itemTextColor
    }

    override fun setItemTextColor(color: Int) {
        itemTextColor = color
        invalidate()
    }

    override fun getItemTextSize(): Int {
        return itemTextSize
    }

    override fun setItemTextSize(size: Int) {
        itemTextSize = size
        paint.textSize = itemTextSize.toFloat()
        computeTextSize()
        requestLayout()
        invalidate()
    }

    override fun getTypeface(): Typeface? {
        return paint.typeface
    }

    override fun setTypeface(tf: Typeface?) {
        paint.typeface = tf
        computeTextSize()
        requestLayout()
        invalidate()
    }

    interface OnItemSelectedListener {
        fun onItemSelected(picker: WheelPicker?, data: String, position: Int)
    }

    interface OnWheelChangeListener {
        fun onWheelScrolled(offset: Int)
        fun onWheelSelected(position: Int)
        fun onWheelScrollStateChanged(state: Int)
    }

    companion object {
        const val SCROLL_STATE_IDLE = 0
        const val SCROLL_STATE_DRAGGING = 1
        const val SCROLL_STATE_SCROLLING = 2
        private val TAG = WheelPicker::class.java.simpleName
    }

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.DateTimePicker)
        itemTextSize = a.getDimensionPixelSize(
            R.styleable.DateTimePicker_wheel_item_text_size,
            resources.getDimensionPixelSize(R.dimen.dp_12)
        )
        selectedItemTextColor =
            a.getColor(R.styleable.DateTimePicker_wheel_selected_item_text_color, -1)
        itemTextColor = a.getColor(R.styleable.DateTimePicker_wheel_item_text_color, -0x777778)
        fontPath = a.getString(R.styleable.DateTimePicker_wheel_font_path)
        a.recycle()

        // 可见数据项改变后更新与之相关的参数
        // Update relevant parameters when the count of visible item changed
        updateVisibleItemCount()
        paint
        paint.textSize = itemTextSize.toFloat()
        if (fontPath != null) {
            val typeface = Typeface.createFromAsset(context.assets, fontPath)
            setTypeface(typeface)
        }

        // 更新文本对齐方式
        // Update alignment of text
        updateItemTextAlign()

        // 计算文本尺寸
        // Correct sizes of text
        computeTextSize()
        scroller = Scroller(getContext())
        val conf = ViewConfiguration.get(getContext())
        minimumVelocity = conf.scaledMinimumFlingVelocity
        maximumVelocity = conf.scaledMaximumFlingVelocity
        touchSlop = conf.scaledTouchSlop
        rectDrawn = Rect()
        rectCurrentItem = Rect()
        camera = Camera()
        matrixRotate = Matrix()
        matrixDepth = Matrix()
    }
}