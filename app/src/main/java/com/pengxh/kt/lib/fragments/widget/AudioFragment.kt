package com.pengxh.kt.lib.fragments.widget

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import com.pengxh.kt.lib.databinding.FragmentWidgetAudioBinding
import com.pengxh.kt.lite.base.KotlinBaseFragment
import com.pengxh.kt.lite.extensions.createAudioFile
import com.pengxh.kt.lite.extensions.millsToTime
import com.pengxh.kt.lite.widget.audio.AudioPopupWindow
import com.pengxh.kt.lite.widget.audio.AudioRecorder
import java.io.File

class AudioFragment : KotlinBaseFragment<FragmentWidgetAudioBinding>() {

    private val audioRecorder by lazy { AudioRecorder(requireContext()) }

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentWidgetAudioBinding {
        return FragmentWidgetAudioBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarLayout() {

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initOnCreate(savedInstanceState: Bundle?) {
        AudioPopupWindow.Builder()
            .setContext(requireContext())
            .setOnAudioPopupCallback(object : AudioPopupWindow.OnAudioPopupCallback {
                override fun onViewCreated(
                    window: PopupWindow, imageView: ImageView, textView: TextView
                ) {
                    binding.recodeAudioButton.setOnTouchListener { _, event ->
                        when (event.action) {
                            MotionEvent.ACTION_DOWN -> {
                                binding.recodeAudioButton.animate()
                                    .scaleX(0.75f).scaleY(0.75f)
                                    .setDuration(100).start()
                                window.showAtLocation(binding.rootView, Gravity.CENTER, 0, 0)
                                //开始录音
                                audioRecorder.apply {
                                    initRecorder(requireContext().createAudioFile())
                                    startRecord(object : AudioRecorder.OnAudioStateUpdateListener {
                                        override fun onUpdate(db: Double, time: Long) {
                                            imageView.drawable.level = (1000 + 60 * db).toInt()
                                            textView.text = time.millsToTime()
                                        }

                                        override fun onStop(file: File?) {
                                            file?.apply {
                                                binding.audioPathView.text =
                                                    "录音文件路径：\r\n${absolutePath}"
                                                binding.audioPlayView.setAudioSource(this)
                                            }
                                        }
                                    })
                                }
                            }

                            MotionEvent.ACTION_UP -> {
                                audioRecorder.stopRecord() //结束录音（保存录音文件）
                                window.dismiss()
                                binding.recodeAudioButton.animate()
                                    .scaleX(1.0f).scaleY(1.0f)
                                    .setDuration(100).start()
                            }
                        }
                        true
                    }
                }
            }).build().create()
    }

    override fun observeRequestState() {

    }

    override fun initEvent() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.audioPlayView.stop()
    }
}