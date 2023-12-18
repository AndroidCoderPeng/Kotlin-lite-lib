package com.pengxh.kt.lib

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import com.pengxh.kt.lib.databinding.ActivityMainBinding
import com.pengxh.kt.lite.base.KotlinBaseActivity
import com.pengxh.kt.lite.extensions.createAudioFile
import com.pengxh.kt.lite.extensions.millsToTime
import com.pengxh.kt.lite.widget.audio.AudioPopupWindow
import com.pengxh.kt.lite.widget.audio.AudioRecordHub
import pub.devrel.easypermissions.EasyPermissions
import java.io.File

class MainActivity : KotlinBaseActivity<ActivityMainBinding>(),
    EasyPermissions.PermissionCallbacks {

    private val kTag = "MainActivity"
    private val context = this@MainActivity
    private val permissionCode = 20231211
    private val userPermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.MANAGE_EXTERNAL_STORAGE,
                Manifest.permission.READ_MEDIA_AUDIO
            )
        } else {
            arrayOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.MANAGE_EXTERNAL_STORAGE
            )
        }
    } else {
        arrayOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    private var recordHub: AudioRecordHub? = null

    override fun initViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }


    override fun setupTopBarLayout() {

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initOnCreate(savedInstanceState: Bundle?) {
        if (EasyPermissions.hasPermissions(this, *userPermissions)) {
            initAudioRecord()
        } else {
            EasyPermissions.requestPermissions(
                this@MainActivity,
                resources.getString(R.string.app_name) + "需要获取相关权限",
                permissionCode,
                *userPermissions
            )
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initAudioRecord() {
        recordHub = AudioRecordHub.get(this)

        AudioPopupWindow.get(this).create(object : AudioPopupWindow.IAudioPopupCallback {
            override fun onViewCreated(
                window: PopupWindow, imageView: ImageView, textView: TextView
            ) {
                binding.recodeAudioButton.setOnTouchListener { v, event ->
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            binding.recodeAudioButton.animate()
                                .scaleX(0.75f).scaleY(0.75f)
                                .setDuration(100).start()
                            window.showAtLocation(binding.rootView, Gravity.CENTER, 0, 0)
                            recordHub?.apply {
                                initHub(createAudioFile())
                                startRecord(object : AudioRecordHub.OnAudioStatusUpdateListener {
                                    override fun onUpdate(db: Double, time: Long) {
                                        imageView.drawable.level = (3000 + 6000 * db / 100).toInt()
                                        textView.text = time.millsToTime()
                                    }

                                    override fun onStop(file: File?) {
                                        if (file != null) {
                                            binding.audioPathView.text =
                                                "录音文件路径：\r\n${file.absolutePath}"
                                            binding.audioPlayView.setAudioSource(file)
                                        }
                                    }
                                })
                            }
                        }

                        MotionEvent.ACTION_UP -> {
                            recordHub?.stopRecord() //结束录音（保存录音文件）
                            window.dismiss()
                            binding.recodeAudioButton.animate()
                                .scaleX(1.0f).scaleY(1.0f)
                                .setDuration(100).start()
                        }
                    }
                    true
                }
            }
        })
    }

    override fun observeRequestState() {

    }

    override fun initEvent() {

    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //将请求结果传递EasyPermission库处理
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        initAudioRecord()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {

    }

    override fun onDestroy() {
        super.onDestroy()
        binding.audioPlayView.stop()
    }
}