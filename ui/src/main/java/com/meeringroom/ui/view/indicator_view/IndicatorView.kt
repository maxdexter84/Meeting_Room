package com.meeringroom.ui.view.indicator_view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.withStyledAttributes
import com.meetingroom.ui.R


@SuppressLint("ResourceAsColor", "ResourceType", "ClickableViewAccessibility")
class IndicatorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var currentY: Float = 0f
    private var currentX: Float = 0f
    private var rectangleHeight = 0f
    private val pattern = "[0-9]{2}:[0-9]{2}".toRegex()
    var startTimeRange: String = ""
        set(value) {
            field = if (value.contains(pattern)) value else "00:00"
            invalidate()
        }
    var endTimeRange = ""
        set(value) {
            field = if (value.contains(pattern)) value else "00:00"
            invalidate()
        }
    private var indicatorColor = 0
    private var thumbColor = 0
    private var indicatorStrokeWidth = 0f
    private var figure: Square? = null
    private var currentYTop = 0f
    private var currentYBottom = 0f
    private var thumbTop = CircleThumb()
    private var thumbBottom = CircleThumb()
    private var motionTouchEventY = 0f
    private var motionTouchEventX = 0f
    private val startPadding = 4f
    private val endPadding = if (startPadding > 0) startPadding * 2 else startPadding
    private var rectangleCreated: Boolean = false
    private val normalRectHeight: Int by lazy {
        getMinuteHeight() * 60
    }


    init {
        this.subscribe()
        isClickable = true
        setWillNotDraw(false)
        context.withStyledAttributes(attrs, R.styleable.IndicatorView) {
            indicatorColor =
                getColor(R.styleable.IndicatorView_indicatorColor, R.color.indicatorColor)
            thumbColor = getColor(R.styleable.IndicatorView_thumbColor, R.color.indicatorColor)
            indicatorStrokeWidth = getDimension(R.styleable.IndicatorView_indicatorStrokeWidth, 2f)
            startTimeRange = getString(R.styleable.IndicatorView_startPeriod).toString()
            endTimeRange = getString(R.styleable.IndicatorView_endPeriod).toString()
        }

    }

    private fun getMinuteHeight(): Int {
        var period = 0
        if (startTimeRange.contains(pattern) && endTimeRange.contains(pattern)) {
            period = IndicatorUtils.getTimePeriod(startTimeRange, endTimeRange)
        }
        return if (period != 0) height / period else 1
    }


    private val rectanglePaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = indicatorStrokeWidth
        color = indicatorColor
    }
    private val thumbPaint = Paint().apply {
        color = thumbColor
        style = Paint.Style.FILL_AND_STROKE
        strokeWidth = indicatorStrokeWidth
        strokeJoin = Paint.Join.ROUND
        isAntiAlias = true
    }


    override fun onDraw(canvas: Canvas) {
        drawFigure(canvas)
    }

    private fun drawFigure(canvas: Canvas) {
        figure?.let {
            canvas.drawRect(
                it.x,
                it.y,
                it.x + it.width,
                it.y + it.height,
                it.color
            )
            canvas.drawCircle(thumbTop.x, thumbTop.y, thumbTop.radius, thumbPaint)
            canvas.drawCircle(thumbBottom.x, thumbBottom.y, thumbBottom.radius, thumbPaint)
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        motionTouchEventY = event.y
        motionTouchEventX = event.x
        listView.onEach {
            if (it != this) it.clearFigure()
        }
        when (event.action) {
            MotionEvent.ACTION_UP -> {
                this.parent.requestDisallowInterceptTouchEvent(true)
                touchStart()
            }
            MotionEvent.ACTION_MOVE -> {
                this.parent.requestDisallowInterceptTouchEvent(true)
                touchMove()
            }
            else -> {
            }
        }
        return true
    }

    private fun touchMove() {
        if (IndicatorUtils.isPointInCircle(
                motionTouchEventX,
                motionTouchEventY,
                thumbTop.x,
                thumbTop.y,
                thumbTop.radius * 5
            ) && motionTouchEventY >= 0 && motionTouchEventY < thumbBottom.y - (getMinuteHeight() * 15)
        ) {
            currentX = motionTouchEventX
            currentY = motionTouchEventY
            val distanceBetweenTopAndBottom = thumbBottom.y - currentY
            createSquare(distanceBetweenTopAndBottom)
        } else if (IndicatorUtils.isPointInCircle(
                motionTouchEventX,
                motionTouchEventY,
                thumbBottom.x,
                thumbBottom.y,
                thumbBottom.radius * 5
            ) && motionTouchEventY <= height && motionTouchEventY > thumbTop.y + (getMinuteHeight() * 15)
        ) {
            val distanceBetweenTopAndBottom = motionTouchEventY - thumbTop.y
            createSquare(distanceBetweenTopAndBottom)
        }
    }

    private fun touchStart() {
        if (!rectangleCreated) {
            currentX = motionTouchEventX
            currentY = if (motionTouchEventY < normalRectHeight) 0f else motionTouchEventY
            val distanceToTheBottom = measuredHeight - currentY
            rectangleHeight = findRectangleHeight(distanceToTheBottom)
            createSquare(rectangleHeight)
        } else if ((motionTouchEventY > currentYTop && motionTouchEventY < currentYBottom) && rectangleCreated) {
            if (!IndicatorUtils.isPointInCircle(
                    motionTouchEventX,
                    motionTouchEventY,
                    thumbBottom.x,
                    thumbBottom.y,
                    thumbBottom.radius * 5
                ) &&
                !IndicatorUtils.isPointInCircle(
                    motionTouchEventX,
                    motionTouchEventY,
                    thumbTop.x,
                    thumbTop.y,
                    thumbTop.radius * 5
                )
            ) {
                performClick()
            }
        } else {
            clearFigure()
        }
    }

    private fun clearFigure() {
        figure = null
        currentYTop = 0f
        currentYBottom = 0f
        rectangleCreated = false

        postInvalidate()
    }

    private fun createSquare(height: Float) {
        figure = Square(
            x = 0 + startPadding,
            y = currentY,
            color = rectanglePaint,
            width = width.toFloat() - endPadding,
            height = height
        ).also {
            createNewThumb(it)
        }
        currentYTop = currentY - 50
        currentYBottom = currentY + height + 50
        rectangleCreated = true
        invalidate()
    }

    private fun findRectangleHeight(distanceToTheBottom: Float) =
        if (distanceToTheBottom < normalRectHeight) distanceToTheBottom
        else if (measuredHeight < normalRectHeight) measuredHeight.toFloat()
        else normalRectHeight.toFloat()

    private fun createNewThumb(figure: Square) {
        thumbBottom =
            CircleThumb(
                figure.x + figure.width - 50,
                figure.y + figure.height,
                figure.color,
                8f
            )
        thumbTop = CircleThumb(figure.x + 50, figure.y, figure.color, 8f)

    }

    override fun performClick(): Boolean {
        Toast.makeText(context, "Click", Toast.LENGTH_SHORT).show()
        return super.performClick()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        unSubscribe()
    }

    companion object {
        val listView = mutableListOf<IndicatorView>()
    }


    private fun subscribe() {
        listView.add(this)
    }

    private fun unSubscribe() {
        listView.remove(this)
    }

}