package com.meeringroom.ui.view.indicator_view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.withStyledAttributes
import com.example.core_module.utils.stringToDateTime
import com.example.core_module.utils.timeToString
import com.meetingroom.ui.R
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import java.time.LocalTime


class IndicatorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private val job = CoroutineScope(SupervisorJob() + Dispatchers.Main)


    private var currentY: Float = 0f
    private var currentX: Float = 0f
    private var rectangleHeight = 0
    var startTimeRange = LocalTime.now().timeToString(TIME_FORMAT)
        set(value) {
            field = value
            invalidate()
        }
    var endTimeRange = LocalTime.now().timeToString(TIME_FORMAT)
        set(value) {
            field = value
            invalidate()
        }
    private var indicatorColor = 0
    private var thumbColor = 0
    private var frameStrokeWidth = 0f
    private var figure: Square? = null
    private var currentYTop = 0f
    private var currentYBottom = 0f
    private var thumbTop = CircleThumb()
    private var thumbBottom = CircleThumb()
    private var motionTouchEventY = 0f
    private var motionTouchEventX = 0f
    private val startPadding = 4f
    private val endPadding = if (startPadding > 0) startPadding * 2 else startPadding
    private var topThumbPadding = 35
        set(value) {
            if (value in 0 until width) field = value
        }
    private var bottomThumbPadding = 35
        set(value) {
            if (value in 0 until width) field = value
        }
    private var rectangleCreated: Boolean = false
    private var normalRectHeight: Int = resources.getDimensionPixelSize(DEFAULT_HOUR_HEIGHT_ID)

    init {
        this.subscribe()
        isClickable = true
        setWillNotDraw(false)
        context.withStyledAttributes(attrs, R.styleable.IndicatorView) {
            indicatorColor =
                getColor(
                    R.styleable.IndicatorView_indicatorColor,
                    resources.getColor(R.color.indicatorColor, context.theme)
                )
            thumbColor = getColor(
                R.styleable.IndicatorView_thumbColor,
                resources.getColor(R.color.indicatorColor, context.theme)
            )
            frameStrokeWidth =
                getDimension(R.styleable.IndicatorView_indicatorStrokeWidth, FRAME_WIDTH)
            startTimeRange = getString(R.styleable.IndicatorView_startPeriod).toString()
            endTimeRange = getString(R.styleable.IndicatorView_endPeriod).toString()
            normalRectHeight = getDimensionPixelSize(
                R.styleable.IndicatorView_hourFrameHeight,
                resources.getDimensionPixelSize(DEFAULT_HOUR_HEIGHT_ID)
            )
            topThumbPadding = getDimensionPixelSize(
                R.styleable.IndicatorView_topThumbPadding,
                DEFAULT_THUMB_PADDING
            )
            bottomThumbPadding = getDimensionPixelSize(
                R.styleable.IndicatorView_bottomThumbPadding,
                DEFAULT_THUMB_PADDING
            )
        }
    }

    private fun getMinuteHeight(): Float = normalRectHeight / MINUTE_IN_HOUR

    private val rectanglePaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = frameStrokeWidth
        color = indicatorColor
    }
    private val thumbPaint = Paint().apply {
        color = thumbColor
        style = Paint.Style.FILL_AND_STROKE
        strokeWidth = frameStrokeWidth
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
                Log.i("ACTION", "${event.action}")
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
                thumbTop.radius * THUMB_INVISIBLE_AREA
            ) && motionTouchEventY >= 0 && motionTouchEventY < thumbBottom.y - (getMinuteHeight() * MIN_MINUTE_INTERVAL)
        ) {
            currentX = motionTouchEventX
            currentY = filterMoveCoord(motionTouchEventY)
            val distanceBetweenTopAndBottom = thumbBottom.y - currentY
            createSquare(distanceBetweenTopAndBottom)
        } else if (IndicatorUtils.isPointInCircle(
                motionTouchEventX,
                motionTouchEventY,
                thumbBottom.x,
                thumbBottom.y,
                thumbBottom.radius * THUMB_INVISIBLE_AREA
            ) && motionTouchEventY <= height && motionTouchEventY > thumbTop.y + (getMinuteHeight() * MIN_MINUTE_INTERVAL)
        ) {
            val distanceBetweenTopAndBottom = filterMoveCoord(motionTouchEventY) - thumbTop.y
            createSquare(distanceBetweenTopAndBottom)
        } else this.parent.requestDisallowInterceptTouchEvent(false)
    }

    private fun touchStart() {
        if (!rectangleCreated) {
            currentX = motionTouchEventX
            currentY = filterStartCoord(motionTouchEventY)
            val distanceToTheBottom = height - currentY
            rectangleHeight = findRectangleHeight(distanceToTheBottom.toInt())
            createSquare(rectangleHeight.toFloat())
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
            emitRangePeriod(null, null)
            clearFigure()
        }
    }

    private fun filterStartCoord(touchEventY: Float): Float {
        return if (touchEventY % (getMinuteHeight() * MIN_MINUTE_INTERVAL) != 0f) {
            val dif = touchEventY % (getMinuteHeight() * MIN_MINUTE_INTERVAL)
            if (touchEventY - dif < 0 || touchEventY < normalRectHeight) 0f
            else touchEventY - dif
        } else touchEventY
    }

    private fun filterMoveCoord(touchEvent: Float): Float {
        return if (touchEvent % (getMinuteHeight() * MIN_MINUTE_STEP) != 0f) {
            val dif = touchEvent % (getMinuteHeight() * MIN_MINUTE_STEP)
            if (touchEvent - dif < 0) 0f
            else touchEvent - dif
        } else touchEvent
    }

    private fun clearFigure() {
        figure = null
        currentYTop = 0f
        currentYBottom = 0f
        rectangleCreated = false
        invalidate()
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
        currentYTop = currentY - FRAME_INVISIBLE_AREA
        currentYBottom = currentY + height + FRAME_INVISIBLE_AREA
        rectangleCreated = true
        invalidate()
    }

    private fun findRectangleHeight(distanceToTheBottom: Int) =
        when {
            distanceToTheBottom < normalRectHeight -> distanceToTheBottom
            height < normalRectHeight -> height
            else -> {
                normalRectHeight
            }
        }


    private fun createNewThumb(figure: Square) {
        thumbBottom =
            CircleThumb(
                figure.x + figure.width - bottomThumbPadding,
                figure.y + figure.height,
                figure.color,
                DEFOULT_THUMB_RADIUS
            )
        thumbTop =
            CircleThumb(figure.x + topThumbPadding, figure.y, figure.color, DEFOULT_THUMB_RADIUS)
        mapCoordinatesToTime(thumbTop, thumbBottom)
    }

    private fun mapCoordinatesToTime(thumbTop: CircleThumb, thumbBottom: CircleThumb) {
        val topDynamicTime = startTimeRange.stringToDateTime(TIME_DATE_FORMAT).toLocalTime()
        val topMinute = when {
            thumbTop.y > 0 -> thumbTop.y / getMinuteHeight()
            else -> 0
        }
        val bottomMinute = thumbBottom.y / getMinuteHeight()
        val resultTopTime = topDynamicTime.plusMinutes(topMinute.toLong())
        val resultBottomTime = topDynamicTime.plusMinutes(bottomMinute.toLong())
        emitRangePeriod(resultTopTime, resultBottomTime)
    }

    private fun emitRangePeriod(
        resultTopTime: LocalTime?,
        resultBottomTime: LocalTime?
    ) {
        job.launch {
            mutableRangePeriod.emit(Pair(resultTopTime, resultBottomTime))
        }
    }

    override fun performClick(): Boolean {
        Toast.makeText(context, "Click", Toast.LENGTH_SHORT).show()
        return super.performClick()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        unSubscribe()
        job.cancel()
    }

    companion object {
        val listView = mutableListOf<IndicatorView>()
        private val mutableRangePeriod = MutableSharedFlow<Pair<LocalTime?, LocalTime?>>()
        val rangePeriod: SharedFlow<Pair<LocalTime?, LocalTime?>>
            get() = mutableRangePeriod
        private val DEFAULT_HOUR_HEIGHT_ID = R.dimen.dimens_66_dp
        private const val TIME_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"
        private const val TIME_FORMAT = "HH:mm"
        private const val MIN_MINUTE_INTERVAL = 15
        private const val MIN_MINUTE_STEP = 5
        private const val DEFAULT_THUMB_PADDING = 35
        private const val FRAME_INVISIBLE_AREA = 50
        private const val THUMB_INVISIBLE_AREA = 5
        private const val MINUTE_IN_HOUR = 60f
        private const val FRAME_WIDTH = 2f
        private const val DEFOULT_THUMB_RADIUS = 8f


    }

    private fun subscribe() {
        listView.add(this)
    }

    private fun unSubscribe() {
        listView.remove(this)
    }

}