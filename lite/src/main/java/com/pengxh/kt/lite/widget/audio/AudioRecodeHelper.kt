package com.pengxh.kt.lite.widget.audio

import android.media.MediaRecorder
import android.os.Handler
import android.os.Looper
import com.pengxh.kt.lite.utils.Constant
import java.io.File
import java.io.IOException
import kotlin.math.log10

class AudioRecodeHelper {
    private var mMediaRecorder: MediaRecorder? = null
    private var filePath: String? = null
    private var audioStatusUpdateListener: OnAudioStatusUpdateListener? = null
    private var startTime: Long = 0

    /**
     * 开始录音 使用m4a格式
     *
     * @return
     */
    fun startRecordAudio(filePath: String?) {
        this.filePath = filePath
        if (mMediaRecorder == null) mMediaRecorder = MediaRecorder()
        try {
            mMediaRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC) // 设置麦克风
            mMediaRecorder!!.setOutputFormat(
                MediaRecorder.OutputFormat.DEFAULT
            )
            mMediaRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            mMediaRecorder!!.setOutputFile(filePath)
            mMediaRecorder!!.setMaxDuration(Constant.MAX_LENGTH)
            mMediaRecorder!!.prepare()
            mMediaRecorder!!.start()
            startTime = System.currentTimeMillis()
            updateMicStatus()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * 停止录音
     */
    fun stopRecordAudio() {
        if (mMediaRecorder == null) {
            return
        }
        try {
            mMediaRecorder!!.stop()
            mMediaRecorder!!.reset()
            mMediaRecorder!!.release()
            mMediaRecorder = null
            audioStatusUpdateListener!!.onStop(filePath)
            filePath = ""
        } catch (e: RuntimeException) {
            mMediaRecorder!!.reset()
            mMediaRecorder!!.release()
            mMediaRecorder = null
            val file = File(filePath!!)
            if (file.exists()) {
                file.delete()
            }
            filePath = ""
        }
    }

    fun setOnAudioStatusUpdateListener(statusUpdateListener: OnAudioStatusUpdateListener?) {
        audioStatusUpdateListener = statusUpdateListener
    }

    /**
     * 更新麦克状态
     */
    private fun updateMicStatus() {
        if (mMediaRecorder != null) {
            val ratio = mMediaRecorder!!.maxAmplitude.toDouble()
            val db: Double // 分贝
            if (ratio > 1) {
                db = 20 * log10(ratio)
                if (null != audioStatusUpdateListener) {
                    audioStatusUpdateListener!!.onUpdate(db, System.currentTimeMillis() - startTime)
                }
            }
            mHandler.postDelayed(mUpdateMicStatusTimer, 100)
        }
    }

    private val mHandler = Handler(Looper.getMainLooper())
    private val mUpdateMicStatusTimer = Runnable { updateMicStatus() }

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
         * @param filePath 保存路径
         */
        fun onStop(filePath: String?)
    }
}