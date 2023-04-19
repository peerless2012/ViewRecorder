package com.peerless2012.recorder.view

import android.Manifest
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Surface
import android.view.SurfaceView
import android.view.TextureView
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import permissions.dispatcher.*
import java.io.File

/**
 * @Author peerless2012
 * @Email peerless2012@126.com
 * @DateTime 2023/4/19 10:19
 * @Version V1.0
 * @Description:
 */
@RuntimePermissions
class MainActivity : AppCompatActivity(),
    View.OnClickListener,
    MediaRecorder.OnErrorListener,
    MediaRecorder.OnInfoListener {

    private lateinit var surfaceView: SurfaceView

    private lateinit var textureView: TextureView

    private lateinit var prepareButton: Button

    private lateinit var startButton: Button

    private lateinit var stopButton: Button

    private val mediaRecorder: MediaRecorder = MediaRecorder()

    private lateinit var surface: Surface

    private lateinit var animator: ObjectAnimator

    private lateinit var viewRender: SurfaceRender

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        surfaceView = findViewById(R.id.main_surface_view)
        surfaceView.holder.addCallback(SurfaceViewCallback())
        textureView = findViewById(R.id.main_texture_view)
        textureView.surfaceTextureListener = TextureViewCallback()
        prepareButton = findViewById(R.id.main_prepare)
        prepareButton.setOnClickListener(this)
        startButton = findViewById(R.id.main_start)
        startButton.setOnClickListener(this)
        stopButton = findViewById(R.id.main_stop)
        stopButton.setOnClickListener(this)
        mediaRecorder.setOnErrorListener(this)
        val view = findViewById<ImageView>(R.id.main_image_view)
        animator = ObjectAnimator.ofFloat(view, "rotation", 360f).also {
            it.repeatCount = ValueAnimator.INFINITE
            it.duration = 5000
        }

        val recordView: View = findViewById(R.id.main_root)
        viewRender = ViewSurfaceRender(recordView)
    }

    override fun onResume() {
        super.onResume()
        animator.start()
    }

    override fun onPause() {
        animator.cancel()
        super.onPause()
    }

    override fun onClick(v: View) {
        when(v.id) {
            R.id.main_prepare -> {
                prepareWithPermissionCheck()
            }
            R.id.main_start -> {
                start()
            }
            R.id.main_stop -> {
                stop()
            }
        }
    }

    @NeedsPermission(Manifest.permission.RECORD_AUDIO)
    fun prepare() {
        try {
            // source
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE)
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)

            // Audio
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            mediaRecorder.setAudioChannels(2)
            mediaRecorder.setAudioEncodingBitRate(96000)
            mediaRecorder.setAudioSamplingRate(44100)

            // Video
            mediaRecorder.setVideoFrameRate(24)
            mediaRecorder.setVideoSize(RECORD_WIDTH, RECORD_HEIGHT)
            mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264)

            val outputFile = File(filesDir, "${System.currentTimeMillis()}.mp4")
            mediaRecorder.setOutputFile(outputFile.absolutePath)

            mediaRecorder.prepare()

            surface = mediaRecorder.surface

            prepareButton.isEnabled = false
            startButton.isEnabled = true
        } catch (pe: Exception) {
            pe.printStackTrace()
            reset()
        }
    }

    @OnShowRationale(Manifest.permission.RECORD_AUDIO)
    fun showRationaleForCamera(permissionRequest: PermissionRequest) {
        Toast.makeText(applicationContext, "Prepare need record audio permission.", Toast.LENGTH_SHORT).show()
    }

    @OnPermissionDenied(Manifest.permission.RECORD_AUDIO)
    fun onCameraDenied() {
        Toast.makeText(applicationContext, "Prepare need record audio permission.", Toast.LENGTH_SHORT).show()
    }

    @OnNeverAskAgain(Manifest.permission.RECORD_AUDIO)
    fun onCameraNeverAskAgain() {
        Toast.makeText(applicationContext, "Prepare need record audio permission.", Toast.LENGTH_SHORT).show()
    }

    private fun start() {
        mediaRecorder.start()
        viewRender.onSurfaceCreated(surface)
        viewRender.onSurfaceChanged(surface, RECORD_WIDTH, RECORD_HEIGHT)
        startButton.isEnabled = false
        stopButton.isEnabled = true
    }

    private fun stop() {
        viewRender.onSurfaceDestroy(surface)
        mediaRecorder.stop()
        reset()
    }

    private fun reset() {
        mediaRecorder.reset()
        prepareButton.isEnabled = true
        startButton.isEnabled = false
        stopButton.isEnabled = false
    }

    override fun onDestroy() {
        mediaRecorder.release()
        super.onDestroy()
    }

    override fun onError(mr: MediaRecorder, what: Int, extra: Int) {
        Log.i("ViewRecorder", "onError what = $what, extra = $extra")
        reset()
    }

    override fun onInfo(mr: MediaRecorder, what: Int, extra: Int) {
        Log.i("ViewRecorder", "onInfo what = $what, extra = $extra")
    }
}