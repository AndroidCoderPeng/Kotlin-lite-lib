package com.pengxh.kt.lite.widget.audio

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import android.os.Handler
import android.os.Looper
import com.pengxh.kt.lite.base.BaseSingleton
import com.pengxh.kt.lite.utils.Constant
import java.io.File
import kotlin.math.log10

/**
 * 音频录制类
 *
 * 1、声明变量
 * private var recordHub: AudioRecordHub? = null
 *
 * 2、授权并初始化
 * recordHub = AudioRecordHub.get(this)
 *
 * 3、分别处理按钮按下和松开逻辑
 *
 *                  binding.audioButton.setOnTouchListener { v, event ->
 *                     when (event.action) {
 *                         MotionEvent.ACTION_DOWN -> {
 *                             window.showAtLocation(binding.rootView, Gravity.CENTER, 0, 0)
 *
 *                             recordHub?.apply {
 *                                 initHub(createAudioFile())
 *                                 startRecord(object :
 *                                     AudioRecordHub.OnAudioStatusUpdateListener {
 *                                     override fun onUpdate(db: Double, time: Long) {
 *                                         imageView.drawable.level = (3000 + 6000 * db / 100).toInt()
 *                                         textView.text = time.millsToTime()
 *                                     }
 *
 *                                     override fun onStop(file: File?) {
 *                                         Log.d(kTag, "onStop => ${file?.absolutePath}")
 *                                     }
 *                                 })
 *                             }
 *                         }
 *
 *                         MotionEvent.ACTION_UP -> {
 *                             recordHub?.stopRecord()
 *                             window.dismiss()
 *                         }
 *                     }
 *                     true
 *                 }
 *
 * */
class AudioRecordHub private constructor(private val context: Context) {

    private val kTag = "AudioRecordHub"

    companion object : BaseSingleton<Context, AudioRecordHub>() {
        override val creator: (Context) -> AudioRecordHub
            get() = ::AudioRecordHub
    }

    private var mediaRecorder: MediaRecorder? = null
    private var audioFile: File? = null
    private var updateStatusHandler: Handler = Handler(Looper.getMainLooper())
    private lateinit var audioStatusUpdateListener: OnAudioStatusUpdateListener
    private var startTime: Long = 0
    private val updateStatusRunnable = Runnable { updateMicStatus() }

    /**
     * 设置保存文件路径，mediaRecorder初始化
     * */
    fun initHub(audioFile: File): AudioRecordHub {
        this.audioFile = audioFile
        mediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            MediaRecorder()
        }
        mediaRecorder?.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC) // 设置麦克风
            setOutputFormat(MediaRecorder.OutputFormat.DEFAULT)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(audioFile.absolutePath)
            setMaxDuration(Constant.MAX_LENGTH)
            prepare()
        }
        return this
    }

    /**
     * 开始录音 使用m4a格式
     *
     * @return
     */
    fun startRecord(audioStatusUpdateListener: OnAudioStatusUpdateListener) {
        this.audioStatusUpdateListener = audioStatusUpdateListener
        mediaRecorder?.start()
        startTime = System.currentTimeMillis()
        updateMicStatus()
    }

    /**
     * 更新麦克状态
     */
    private fun updateMicStatus() {
        val ratio = mediaRecorder?.maxAmplitude
        val db = if (ratio == null) {
            0.0
        } else {
            if (ratio > 1) {
                20 * log10(ratio.toDouble())
            } else {
                0.0
            }
        }
        audioStatusUpdateListener.onUpdate(db, System.currentTimeMillis() - startTime)
        updateStatusHandler.postDelayed(updateStatusRunnable, 100)
    }

    /**
     * 停止录音
     */
    fun stopRecord() {
        audioStatusUpdateListener.onStop(audioFile)
        mediaRecorder?.apply {
            stop()
            reset()
            release()
        }
        mediaRecorder = null
        audioFile = null
        updateStatusHandler.removeCallbacks(updateStatusRunnable)
    }

    interface OnAudioStatusUpdateListener {
        /**
         * 录音中...
         *
         * @param db   当前声音分贝
         * @param time 录音时长
         */
        fun onUpdate(db: Double, time: Long)

        /**
         * 停止录音
         *
         * @param file 保存文件
         */
        fun onStop(file: File?)
    }
}