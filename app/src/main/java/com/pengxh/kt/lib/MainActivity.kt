package com.pengxh.kt.lib

import android.Manifest
import android.graphics.Color
import android.os.Bundle
import com.pengxh.kt.lib.databinding.ActivityMainBinding
import com.pengxh.kt.lite.base.KotlinBaseActivity
import pub.devrel.easypermissions.EasyPermissions

class MainActivity : KotlinBaseActivity<ActivityMainBinding>(),
    EasyPermissions.PermissionCallbacks {

    private val kTag = "MainActivity"
    private val context = this@MainActivity
    private val permissionCode = 20231211
    private val userPermissions = arrayOf(
        Manifest.permission.RECORD_AUDIO
    )

    override fun initViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }


    override fun setupTopBarLayout() {

    }

    override fun initOnCreate(savedInstanceState: Bundle?) {
        if (EasyPermissions.hasPermissions(this, *userPermissions)) {
            binding.airDashBoardView
                .setMaxValue(500)
                .setCenterText("优")
                .setAirRingForeground(Color.GREEN)
                .setAirCenterTextColor(Color.RED)
                .setAirCurrentValueColor(Color.BLUE)
                .setCurrentValue(255)
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

    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {

    }
}