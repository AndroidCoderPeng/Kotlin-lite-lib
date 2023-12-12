package com.pengxh.kt.lib

import android.Manifest
import android.os.Bundle
import com.pengxh.kt.lib.databinding.ActivityMainBinding
import com.pengxh.kt.lite.base.KotlinBaseActivity
import com.pengxh.kt.lite.widget.SteeringWheelView
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
            binding.steeringWheelView.setOnWheelTouchListener(object :
                SteeringWheelView.OnWheelTouchListener {
                override fun onCenterClicked() {

                }

                override fun onLeftTurn() {

                }

                override fun onTopTurn() {

                }

                override fun onRightTurn() {

                }

                override fun onBottomTurn() {

                }

                override fun onActionTurnUp(dir: SteeringWheelView.Direction) {

                }
            })
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