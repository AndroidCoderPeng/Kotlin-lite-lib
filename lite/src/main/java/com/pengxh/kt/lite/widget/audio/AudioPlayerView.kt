package com.pengxh.kt.lite.widget.audio

import android.content.Context
import android.graphics.drawable.Drawable
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import com.pengxh.kt.lite.R
import java.io.IOException

class AudioPlayerView(context: Context, attrs: AttributeSet?) : AppCompatTextView(context, attrs) {
    private var mediaPlayer: MediaPlayer? = null

    /**
     * 在非初始化状态下调用setDataSource  会抛出IllegalStateException异常
     */
    private var hasPrepared = false
    private var mUrl: String? = null
    private var index = 0
    private var audioAnimationHandler: Handler? = null
    private var animationRunnable: Runnable? = null

    companion object {
        private val drawables = intArrayOf(
            R.drawable.ic_audio_icon1, R.drawable.ic_audio_icon2, R.drawable.ic_audio_icon3
        )
    }

    init {
        initMediaPlayer()
    }

    private fun initMediaPlayer() {
        try {
            mediaPlayer = MediaPlayer()
            mediaPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
        } catch (e: Exception) {
            Log.e("mediaPlayer", " init error", e)
        }
        if (mediaPlayer != null) {
            mediaPlayer!!.setOnPreparedListener {
                hasPrepared = true
                text = audioDuration
            }
            mediaPlayer!!.setOnErrorListener { mp, _, _ ->
                mp.reset()
                false
            }
            mediaPlayer!!.setOnCompletionListener { stopAnimation() }
        }
        setViewClick()
    }

    private val audioDuration: String
        get() {
            val duration = mediaPlayer!!.duration
            return if (duration == -1) {
                ""
            } else {
                val sec = duration / 1000
                val m = sec / 60
                val s = sec % 60
                "$m:$s"
            }
        }

    fun setAudioUrl(url: String?) {
        mUrl = url
        try {
            mediaPlayer!!.setDataSource(url)
            mediaPlayer!!.prepare()
        } catch (e: IOException) {
            Log.e("mediaPlayer", " set dataSource error", e)
        } catch (e: IllegalStateException) {
            Log.e("mediaPlayer", " set dataSource error", e)
        }
    }

    /**
     * 用于需要设置不同的dataSource
     * 二次setDataSource的时候需要reset 将MediaPlayer恢复到Initialized状态
     *
     * @param url
     */
    fun resetUrl(url: String?) {
        if (mUrl.toString().isBlank() || hasPrepared) {
            mediaPlayer!!.reset()
        }
        setAudioUrl(url)
    }

    private fun startAnimation() {
        if (audioAnimationHandler == null) {
            audioAnimationHandler = Handler(Looper.getMainLooper())
        }
        if (animationRunnable == null) {
            animationRunnable = object : Runnable {
                override fun run() {
                    audioAnimationHandler!!.postDelayed(this, 200)
                    setDrawable(drawables[index % 3])
                    index++
                }
            }
        }
        audioAnimationHandler!!.removeCallbacks(animationRunnable!!)
        audioAnimationHandler!!.postDelayed(animationRunnable!!, 200)
    }

    private fun stopAnimation() {
        setDrawable(drawables[2])
        if (audioAnimationHandler != null) {
            audioAnimationHandler!!.removeCallbacks(animationRunnable!!)
        }
    }

    //暂时只能设置在左边，后期改为可设置方向
    private fun setDrawable(@DrawableRes id: Int) {
        val drawable: Drawable = ResourcesCompat.getDrawable(context.resources, id, null)!!
        drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
        setCompoundDrawables(drawable, null, null, null)
    }

    private fun setViewClick() {
        setOnClickListener(View.OnClickListener {
            if (mediaPlayer!!.isPlaying) {
                mediaPlayer!!.pause()
                stopAnimation()
            } else {
                mediaPlayer!!.seekTo(0)
                startAnimation()
                mediaPlayer!!.start()
            }
        })
    }

    fun release() {
        if (mediaPlayer != null) {
            mediaPlayer!!.stop()
            mediaPlayer!!.release()
            mediaPlayer = null
        }
        if (audioAnimationHandler != null) {
            audioAnimationHandler!!.removeCallbacks(animationRunnable!!)
        }
    }
}