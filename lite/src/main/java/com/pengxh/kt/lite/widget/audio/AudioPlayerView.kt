package com.pengxh.kt.lite.widget.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import com.pengxh.kt.lite.utils.Constant
import com.pengxh.kt.lite.utils.WeakReferenceHandler

class AudioPlayerView(context: Context, attrs: AttributeSet) : AppCompatTextView(context, attrs),
    Handler.Callback {

    private val kTag = "AudioPlayerView"
    private val mediaPlayer by lazy { MediaPlayer() }
    private val audioAttributes = AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_UNKNOWN)
        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
        .build()

    /**
     * 在非初始化状态下调用setDataSource  会抛出IllegalStateException异常
     */
    private var hasPrepared = false
    private var index = 0
    private var weakReferenceHandler: WeakReferenceHandler
    private var animationRunnable: Runnable

    init {
        mediaPlayer.setAudioAttributes(audioAttributes)
        mediaPlayer.setOnPreparedListener {
            hasPrepared = true
            text = audioDuration
        }
        mediaPlayer.setOnErrorListener { mp, _, _ ->
            mp.reset()
            false
        }
        mediaPlayer.setOnCompletionListener { stopAnimation() }

        setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                stopAnimation()
            } else {
                mediaPlayer.seekTo(0)
                startAnimation()
                mediaPlayer.start()
            }
        }

        weakReferenceHandler = WeakReferenceHandler(this)
        animationRunnable = object : Runnable {
            override fun run() {
                weakReferenceHandler.postDelayed(this, 200)
                setDrawable(Constant.AUDIO_DRAWABLES[index % 3])
                index++
            }
        }
    }

    private val audioDuration: String
        get() {
            val duration = mediaPlayer.duration
            return if (duration == -1) {
                ""
            } else {
                val sec = duration / 1000
                val m = sec / 60
                val s = sec % 60
                "$m:$s"
            }
        }

    fun setAudioSource(audioSource: String) {
        mediaPlayer.setDataSource(audioSource)
        mediaPlayer.prepare()
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

    fun release() {
        mediaPlayer.stop()
        mediaPlayer.release()
        weakReferenceHandler.removeCallbacks(animationRunnable)
    }

    override fun handleMessage(msg: Message): Boolean {
        return true
    }
}