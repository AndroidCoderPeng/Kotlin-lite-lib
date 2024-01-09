package com.pengxh.kt.lite.widget.audio

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.view.View
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import com.pengxh.kt.lite.utils.Constant
import com.pengxh.kt.lite.utils.WeakReferenceHandler
import java.io.File
import java.io.FileInputStream
import java.io.IOException

/**
 * TODO 暂时先放一放，计划重写
 * */
class AudioPlayerView(context: Context, attrs: AttributeSet) : AppCompatTextView(context, attrs),
    Handler.Callback, View.OnClickListener {

    private val kTag = "AudioPlayerView"
    private val weakReferenceHandler by lazy { WeakReferenceHandler(this) }
    private lateinit var audioSource: File
    private var mediaPlayer: MediaPlayer? = null
    private var hasPrepared = false
    private var index = 0
    private var animationRunnable: Runnable

    init {
        setOnClickListener(this)

        animationRunnable = object : Runnable {
            override fun run() {
                weakReferenceHandler.postDelayed(this, 200)
                setDrawable(Constant.AUDIO_DRAWABLES[index % 3])
                index++
            }
        }
    }

    fun setAudioSource(file: File) {
        audioSource = file
        initMediaPlayer()
    }

    private fun initMediaPlayer() {
        mediaPlayer = MediaPlayer()
        //此时MediaPlayer进入Idle状态，不能进行任何操作，setDataSource会让播放器从Idle进入Loaded状态
        mediaPlayer?.apply {
            val inputStream = FileInputStream(audioSource)
            setDataSource(inputStream.fd)
            try {
                prepare()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            setOnPreparedListener {
                hasPrepared = true

                text = if (duration == -1) {
                    ""
                } else {
                    val sec = duration / 1000
                    val m = sec / 60
                    val s = sec % 60
                    "$m:$s"
                }
            }
        }
    }

    override fun onClick(v: View?) {
        mediaPlayer?.apply {
            if (isPlaying) {
                stop()
                release()
                stopAnimation()
                mediaPlayer = null
            } else {
                initMediaPlayer()
                start()
                startAnimation()
            }
        }
    }

    private fun startAnimation() {
        weakReferenceHandler.removeCallbacks(animationRunnable)
        weakReferenceHandler.postDelayed(animationRunnable, 200)
    }

    private fun stopAnimation() {
        setDrawable(Constant.AUDIO_DRAWABLES[2])
        weakReferenceHandler.removeCallbacks(animationRunnable)
    }

    //暂时只能设置在左边，后期改为可设置方向
    private fun setDrawable(@DrawableRes id: Int) {
        val drawable = ResourcesCompat.getDrawable(context.resources, id, null)!!
        drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
        setCompoundDrawables(drawable, null, null, null)
    }

    fun stop() {
        mediaPlayer?.apply {
            stop()
            release()
        }
        weakReferenceHandler.removeCallbacks(animationRunnable)
        mediaPlayer = null
    }

    override fun handleMessage(msg: Message): Boolean {
        return true
    }
}