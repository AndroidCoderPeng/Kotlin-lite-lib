package com.pengxh.kt.lib

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import com.pengxh.kt.lib.databinding.ActivityMainBinding
import com.pengxh.kt.lite.base.KotlinBaseActivity
import com.pengxh.kt.lite.extensions.millsToTime
import com.pengxh.kt.lite.widget.audio.AudioPopupWindow
import com.pengxh.kt.lite.widget.audio.AudioRecordHub
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : KotlinBaseActivity<ActivityMainBinding>(),
    EasyPermissions.PermissionCallbacks {

    private val kTag = "MainActivity"
    private val context = this@MainActivity
    private val permissionCode = 20231211
    private val userPermissions = arrayOf(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.READ_MEDIA_AUDIO,
        Manifest.permission.MANAGE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    private var recordHub: AudioRecordHub? = null

    override fun initViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }


    override fun setupTopBarLayout() {

    }

    override fun initOnCreate(savedInstanceState: Bundle?) {
        if (EasyPermissions.hasPermissions(this, *userPermissions)) {
            recordHub = AudioRecordHub.get(this)
        } else {
            EasyPermissions.requestPermissions(
                this@MainActivity,
                resources.getString(R.string.app_name) + "需要获取相关权限",
                permissionCode,
                *userPermissions
            )
        }
    }

    override fun observeRequestState() {

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initEvent() {
        AudioPopupWindow.get(this).create(object : AudioPopupWindow.IAudioPopupCallback {
            override fun onViewCreated(
                window: PopupWindow, imageView: ImageView, textView: TextView
            ) {
                binding.audioButton.setOnTouchListener { v, event ->
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            window.showAtLocation(binding.rootView, Gravity.CENTER, 0, 0)

                            recordHub?.apply {
                                initHub(createAudioFile())
                                startRecord(object :
                                    AudioRecordHub.OnAudioStatusUpdateListener {
                                    override fun onUpdate(db: Double, time: Long) {
                                        imageView.drawable.level = (3000 + 6000 * db / 100).toInt()
                                        textView.text = time.millsToTime()
                                    }

                                    override fun onStop(file: File?) {
                                        Log.d(kTag, "onStop => ${file?.absolutePath}")
                                    }
                                })
                            }
                        }

                        MotionEvent.ACTION_UP -> {
                            recordHub?.stopRecord()
                            window.dismiss()
                        }
                    }
                    true
                }
            }
        })
    }

    private fun createAudioFile(): File {
        val audioDir = File(getExternalFilesDir(Environment.DIRECTORY_MUSIC), "")
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(Date())
        val audioFile = File(audioDir.toString() + File.separator + "AUD_" + timeStamp + ".m4a")
        if (!audioFile.exists()) {
            try {
                audioFile.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return audioFile
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //将请求结果传递EasyPermission库处理
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        recordHub = AudioRecordHub.get(this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {

    }
}