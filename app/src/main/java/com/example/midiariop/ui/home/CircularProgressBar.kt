package com.example.midiariop.ui.home

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat

class CircularProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var progress = 0f
    private var maxProgress = 2035f

    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, android.R.color.darker_gray)
        style = Paint.Style.STROKE
        strokeWidth = 20f
    }

    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, android.R.color.holo_green_light)
        style = Paint.Style.STROKE
        strokeWidth = 20f
        strokeCap = Paint.Cap.ROUND
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val centerX = width / 2f
        val centerY = height / 2f
        val radius = Math.min(centerX, centerY) - 20f

        // Dibuja el fondo
        canvas.drawCircle(centerX, centerY, radius, backgroundPaint)

        // Dibuja el progreso
        val sweepAngle = (progress / maxProgress) * 360f
        canvas.drawArc(
            centerX - radius,
            centerY - radius,
            centerX + radius,
            centerY + radius,
            -90f, // Empieza en la parte superior
            sweepAngle,
            false,
            progressPaint
        )
    }

    fun setProgressWithAnimation(newProgress: Float) {
        val animator = ValueAnimator.ofFloat(progress, newProgress).apply {
            duration = 1000 // Duración en milisegundos
            addUpdateListener { animation ->
                progress = animation.animatedValue as Float
                invalidate() // Redibuja la vista
            }
            interpolator = android.view.animation.DecelerateInterpolator() // Disminuye la velocidad
        }
        animator.start()
    }

    fun getProgress(): Float {
        return progress
    }

    fun setProgress(newProgress: Float) {
        progress = newProgress
        invalidate() // Redibuja la vista inmediatamente
    }
}

