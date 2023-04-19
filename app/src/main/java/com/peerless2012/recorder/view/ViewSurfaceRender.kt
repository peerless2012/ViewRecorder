package com.peerless2012.recorder.view

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.core.view.doOnLayout

/**
 * @Author peerless2012
 * @Email peerless2012@126.com
 * @DateTime 2023/4/19 16:07
 * @Version V1.0
 * @Description:
 */
class ViewSurfaceRender(private val view: View) : SurfaceRender(view::class.java.name) {

    private var count = 0

    private val matrix = Matrix()

    private fun setupMatrix() {
        matrix.reset()
        val viewWidth = view.measuredWidth
        val viewHeight = view.measuredHeight
        val surfaceWidth = RECORD_WIDTH
        val surfaceHeight = RECORD_HEIGHT
        val surfaceRatio = surfaceHeight / surfaceWidth.toFloat()
        val hp = 0
        val vp = 0
        val viewRatio = (viewHeight - vp) / (viewWidth - hp).toFloat()
        val scale = if (viewRatio >= surfaceRatio) {
            // 内容区域过高
            surfaceHeight / (viewHeight - vp).toFloat()
        } else {
            // 内容区域过宽
            surfaceWidth / (viewWidth - hp).toFloat()
        }
        matrix.postScale(scale, scale)
        val offsetX = (surfaceWidth - (viewWidth - hp) * scale) / 2f
        val offsetY = (surfaceHeight - (viewHeight - vp) * scale) / 2f
        matrix.postTranslate(offsetX, offsetY)
    }

    init {
        renderHandler = Handler(Looper.getMainLooper())
        view.doOnLayout {
            setupMatrix()
        }
    }

    override fun onDrawFrame(canvas: Canvas) {
        canvas.drawColor(Color.WHITE)
        canvas.save()
        canvas.setMatrix(matrix)
        view.draw(canvas)
        canvas.restore()
    }
    
}