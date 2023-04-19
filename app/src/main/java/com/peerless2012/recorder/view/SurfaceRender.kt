package com.peerless2012.recorder.view

import android.graphics.Canvas
import android.graphics.Rect
import android.os.Build
import android.os.Handler
import android.view.Surface

/**
 * @Author peerless2012
 * @Email peerless2012@126.com
 * @DateTime 2023/4/19 11:20
 * @Version V1.0
 * @Description:
 */
abstract class SurfaceRender(name: String) : Runnable {

    private val rect = Rect()

    protected lateinit var renderHandler: Handler

    private var surface: Surface? = null

    fun onSurfaceCreated(surface: Surface) {
        this.surface = surface
        renderHandler.post(this)
    }

    fun onSurfaceChanged(surface: Surface, width: Int, height: Int) {
        rect.set(0, 0, width, height)
    }

    fun onSurfaceDestroy(surface: Surface) {
        rect.setEmpty()
        this.surface = null
        renderHandler.removeCallbacksAndMessages(null)
    }

    override fun run() {
        if (surface == null) return

        renderHandler.postDelayed(this, 30)

        if (rect.isEmpty) return

        var canvas: Canvas? = null
        try {
            canvas = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                surface!!.lockHardwareCanvas()
            } else {
                surface!!.lockCanvas(rect)
            }
            onDrawFrame(canvas)
        } catch (de: Exception) {
            de.printStackTrace()
        } finally {
            canvas?.let {
                try {
                    surface!!.unlockCanvasAndPost(canvas)
                } catch (ue: Exception) {
                    ue.printStackTrace()
                }
            }
        }
    }

    protected abstract fun onDrawFrame(canvas: Canvas)
}