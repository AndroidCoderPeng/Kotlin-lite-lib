package com.pengxh.kt.lite.widget.audio

import android.content.Context
import android.media.AsyncPlayer
import android.media.AudioAttributes
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.CountDownTimer
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.view.View
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import com.pengxh.kt.lite.R
import com.pengxh.kt.lite.extensions.appendZero
import com.pengxh.kt.lite.utils.Constant
import com.pengxh.kt.lite.utils.WeakReferenceHandler
import java.io.File
import java.io.FileInputStream


class AudioPlayerView(context: Context, attrs: AttributeSet) : AppCompatTextView(context, attrs),
    Handler.Callback, View.OnClickListener {

    private val kTag = "AudioPlayerView"
    private val weakReferenceHandler by lazy { WeakReferenceHandler(this) }
    private val asyncPlayer by lazy { AsyncPlayer(kTag) }
    private val audioAttributes = AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_VOICE_COMMUNICATION)
        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
        .build()
    private var index = 0
    private var isPlaying = false
    private var duration = 0L
    private lateinit var file: File

    init {
        setOnClickListener(this)
    }

    fun setAudioSource(file: File) {
        this.file = file
        //获取音频时长
        val mmr = MediaMetadataRetriever()
        try {
            val inputStream = FileInputStream(file)
            val fileDescriptor = inputStream.fd
            mmr.setDataSource(fileDescriptor)
            val time = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            duration = if (time.isNullOrBlank()) {
                0L
            } else {
                time.toLong()
            }

            //格式化时长
            val sec = duration / 1000
            val m = (sec / 60).toInt().appendZero()
            val s = (sec % 60).toInt().appendZero()
            text = "$m:$s"
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            mmr.release()
        }
    }

    override fun onClick(v: View?) {
        if (isPlaying) {
            asyncPlayer.stop()
            stopAnimation()
            isPlaying = false
        } else {
            asyncPlayer.play(context, Uri.fromFile(file), false, audioAttributes)
            startAnimation()
            isPlaying = true
            /**
             * 倒计时，判断音频是否播放完毕
             *
             * AsyncPlayer是MediaPlayer的简单异步封装，简单到只提供播放和停止API，所以需要自己监听播放是否完毕
             * */
            object : CountDownTimer(duration, 1000) {
                override fun onTick(millisUntilFinished: Long) {

                }

                override fun onFinish() {
                    stop()
                }
            }.start()
        }
    }

    private fun startAnimation() {
        //防止被多次点击，每次点击View之后都把之前的动画Runnable清空
        weakReferenceHandler.removeCallbacks(animationRunnable)
        weakReferenceHandler.postDelayed(animationRunnable, 200)
    }

    private fun stopAnimation() {
        setDrawable(R.drawable.ic_audio_icon3)
        weakReferenceHandler.removeCallbacks(animationRunnable)
    }

    private var animationRunnable = object : Runnable {
        override fun run() {
            weakReferenceHandler.postDelayed(this, 200)
            setDrawable(Constant.AUDIO_DRAWABLES[index % 3])
            index++
        }
    }

    private fun setDrawable(@DrawableRes id: Int) {
        val drawable = ResourcesCompat.getDrawable(context.resources, id, null)!!
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        setCompoundDrawables(drawable, null, null, null)
    }

    fun stop() {
        weakReferenceHandler.removeCallbacks(animationRunnable)
        isPlaying = false
    }

    override fun handleMessage(msg: Message): Boolean {
        return true
    }
}