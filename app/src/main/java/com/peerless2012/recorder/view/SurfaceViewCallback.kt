package com.peerless2012.recorder.view

import android.view.SurfaceHolder

/**
 * @Author peerless2012
 * @Email peerless2012@126.com
 * @DateTime 2023/4/19 11:16
 * @Version V1.0
 * @Description:
 */
class SurfaceViewCallback : SurfaceHolder.Callback {

    private val surfaceRender = TextSurfaceRender("SurfaceView")

    override fun surfaceCreated(holder: SurfaceHolder) {
        surfaceRender.onSurfaceCreated(holder.surface)
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        surfaceRender.onSurfaceChanged(holder.surface, width, height)
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        surfaceRender.onSurfaceDestroy(holder.surface)
    }

}