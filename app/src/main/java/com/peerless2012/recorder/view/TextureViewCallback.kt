package com.peerless2012.recorder.view

import android.graphics.SurfaceTexture
import android.view.Surface
import android.view.TextureView

/**
 * @Author peerless2012
 * @Email peerless2012@126.com
 * @DateTime 2023/4/19 11:17
 * @Version V1.0
 * @Description:
 */
class TextureViewCallback : TextureView.SurfaceTextureListener {

    private val surfaceRender = TextSurfaceRender("TextureView")

    private lateinit var surface: Surface

    override fun onSurfaceTextureAvailable(surfaceTexture: SurfaceTexture, width: Int, height: Int) {
        surface = Surface(surfaceTexture)
        surfaceRender.onSurfaceCreated(surface)
        surfaceRender.onSurfaceChanged(surface, width, height)
    }

    override fun onSurfaceTextureSizeChanged(surfaceTexture: SurfaceTexture, width: Int, height: Int) {
        surfaceRender.onSurfaceChanged(surface, width, height)
    }

    override fun onSurfaceTextureDestroyed(surfaceTexture: SurfaceTexture): Boolean {
        surfaceRender.onSurfaceDestroy(surface)
        return true
    }

    override fun onSurfaceTextureUpdated(surfaceTexture: SurfaceTexture) {
    }

}