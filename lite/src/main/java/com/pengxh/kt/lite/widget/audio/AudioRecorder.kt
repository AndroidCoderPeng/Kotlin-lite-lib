package com.pengxh.kt.lite.widget.audio

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import android.os.Handler
import android.os.Message
import com.pengxh.kt.lite.utils.Constant
import com.pengxh.kt.lite.utils.WeakReferenceHandler
import java.io.File
import kotlin.math.log10

/**
 * 音频录制类
 * */
class AudioRecorder constructor(private val context: Context) : Handler.Callback {

    private val stateUpdateHandler by lazy { WeakReferenceHandler(this) }
    private val updateStatusRunnable = Runnable { updateMicStatus() }
    private var mediaRecorder: MediaRecorder? = null
    private var audioFile: File? = null
    private var startTime: Long = 0
    private lateinit var stateUpdateListener: OnAudioStateUpdateListener

    override fun handleMessage(msg: Message): Boolean {
        return true
    }

    /**
     * 设置保存文件路径，mediaRecorder初始化
     * */
    fun initRecorder(audioFile: File) {
        this.audioFile = audioFile
        mediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            MediaRecorder()
        }
        mediaRecorder?.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC) // 设置麦克风
            setOutputFormat(MediaRecorder.OutputFormat.AMR_WB)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB)
            setOutputFile(audioFile.absolutePath)
            setMaxDuration(Constant.MAX_LENGTH)
            prepare()
        }
    }

    /**
     * 开始录音 使用amr格式
     *
     * @return
     */
    fun startRecord(stateUpdateListener: OnAudioStateUpdateListener) {
        this.stateUpdateListener = stateUpdateListener
        mediaRecorder?.start()
        startTime = System.currentTimeMillis()
        updateMicStatus()
    }

    /**
     * 更新麦克状态
     */
    private fun updateMicStatus() {
        //调用时音频采样的最大绝对振幅
        val amplitude = mediaRecorder?.maxAmplitude
        val db = if (amplitude == null) {
            0.0
        } else {
            if (amplitude > 1) {
                20 * log10(amplitude / 0.1)
            } else {
                0.0
            }
        }
        stateUpdateListener.onUpdate(db, System.currentTimeMillis() - startTime)
        stateUpdateHandler.postDelayed(updateStatusRunnable, 100)
    }

    /**
     * 停止录音
     */
    fun stopRecord() {
        stateUpdateListener.onStop(audioFile)
        mediaRecorder?.apply {
            stop()
            reset()
            release()
        }
        mediaRecorder = null
        audioFile = null
        stateUpdateHandler.removeCallbacks(updateStatusRunnable)
    }

    interface OnAudioStateUpdateListener {
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