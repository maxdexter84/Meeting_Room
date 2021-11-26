package com.meeringroom.ui.view.event_view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import com.meetingroom.ui.R

class EventRectangleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defaultAttributeSet: Int = 0,
) : View(context, attrs, defaultAttributeSet) {

    var isUserOwnEvent = false
        set(value) {
            field = value
            invalidate()
        }

    var colorEvent = Color.WHITE
        set(value) {
            field = value
            invalidate()
        }

    private lateinit var painterEvent: Paint
    private lateinit var painterDiagonalLineEvent: Paint

    init {
        context.withStyledAttributes(attrs, R.styleable.EventRectangleView) {
            isUserOwnEvent = getBoolean(R.styleable.EventRectangleView_isUserOwnEvent, false)
            colorEvent = getColor(R.styleable.EventRectangleView_colorEvent, Color.WHITE)
        }
        initPainters()
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.apply {
            drawEventBackground(width.toFloat(), height.toFloat(), canvas)
        }
    }

    private fun drawEventBackground(width: Float, height: Float, canvas: Canvas) {
        canvas.drawRect(0f, 0f, width, height, painterEvent)
        painterDiagonalLineEvent.strokeWidth = width/15f
        val dividingPartScreen = width/3f
        val amountLines = (height/10).toInt()

        if (isUserOwnEvent) {
            for (i in 1..amountLines) {
                canvas.drawLine(dividingPartScreen * i.toFloat(), -height, -width, dividingPartScreen  * i.toFloat(), painterDiagonalLineEvent)
            }
        }
    }

    private fun initPainters() {
        painterEvent = Paint().apply {
            color = colorEvent
            isAntiAlias = true
            style = Paint.Style.FILL
        }

        painterDiagonalLineEvent = Paint().apply {
            color = Color.WHITE
            isAntiAlias = true
            style = Paint.Style.STROKE
            alpha = 150
        }
    }
}