package com.peerless2012.recorder.view

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.os.Handler
import android.os.HandlerThread

/**
 * @Author peerless2012
 * @Email peerless2012@126.com
 * @DateTime 2023/4/19 16:28
 * @Version V1.0
 * @Description:
 */
class TextSurfaceRender(private val name: String) : SurfaceRender(name) {

    private val renderThread = HandlerThread(name).also {
        it.start()
    }

    private val renderType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        "HW Rendering: "
    } else {
        "SW Rendering: "
    }

    private var counter: Int = 1

    init {
        renderHandler = Handler(renderThread.looper)
    }

    private val paint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.FILL
        textSize = 64f
    }

    override fun onDrawFrame(canvas: Canvas) {
        canvas.drawColor(Color.GRAY)
        canvas.drawText(
            name,
            10f,
            60f,
            paint
        )

        canvas.drawText(
            renderType + canvas.isHardwareAccelerated.toString(),
            10f,
            120f,
            paint
        )

        canvas.drawText(
            "Counter: $counter",
            10f,
            180f,
            paint
        )

        counter++
    }

    protected fun finalize() {
        renderThread.quitSafely()
    }

}