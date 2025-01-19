package com.pengxh.kt.lib.fragments.extensions

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.wifi.WifiManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import com.pengxh.kt.lib.R
import com.pengxh.kt.lib.databinding.FragmentExtensionContextBinding
import com.pengxh.kt.lib.example.ActivityExtensionExample
import com.pengxh.kt.lib.view.BigImageActivity
import com.pengxh.kt.lite.base.KotlinBaseFragment
import com.pengxh.kt.lite.extensions.convertColor
import com.pengxh.kt.lite.extensions.createAudioFile
import com.pengxh.kt.lite.extensions.createCompressImageDir
import com.pengxh.kt.lite.extensions.createDownloadFileDir
import com.pengxh.kt.lite.extensions.createImageFileDir
import com.pengxh.kt.lite.extensions.createLogFile
import com.pengxh.kt.lite.extensions.createVideoFileDir
import com.pengxh.kt.lite.extensions.getScreenDensity
import com.pengxh.kt.lite.extensions.getScreenHeight
import com.pengxh.kt.lite.extensions.getScreenWidth
import com.pengxh.kt.lite.extensions.getStatusBarHeight
import com.pengxh.kt.lite.extensions.getSystemService
import com.pengxh.kt.lite.extensions.isNetworkConnected
import com.pengxh.kt.lite.extensions.navigatePageTo
import com.pengxh.kt.lite.extensions.readAssetsFile
import com.pengxh.kt.lite.extensions.show
import com.pengxh.kt.lite.widget.dialog.BottomActionSheet

class ContextExtensionFragment : KotlinBaseFragment<FragmentExtensionContextBinding>() {

    private val kTag = "ContextExtensionFragment"
    private val imageArray = arrayListOf(
        "https://img.zcool.cn/community/010d5c5b9d17c9a8012099c8781b7e.jpg@1280w_1l_2o_100sh.jpg",
        "https://tse4-mm.cn.bing.net/th/id/OIP-C.6szqS1IlGtWsaiHQUtUOVwHaQC?rs=1&pid=ImgDetMain",
        "https://img.zcool.cn/community/01a15855439bdf0000019ae9299cce.jpg@1280w_1l_2o_100sh.jpg",
        "https://pic1.zhimg.com/v2-0cc45f5fda6e8ff79350ec1303835629_r.jpg"
    )

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentExtensionContextBinding {
        return FragmentExtensionContextBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarLayout() {

    }

    override fun initOnCreate(savedInstanceState: Bundle?) {
        if (requireContext().isNetworkConnected()) {
            binding.netStateView.setBackgroundColor(Color.GREEN)
        } else {
            binding.netStateView.setBackgroundColor(Color.RED)
        }

        binding.displaySizeView.text =
            "[${requireContext().getScreenWidth()},${requireContext().getScreenHeight()}]"

        binding.statusBarHeightView.text = "${requireContext().getStatusBarHeight()}"
        binding.screenDensityView.text = "${requireContext().getScreenDensity()}"
    }

    override fun observeRequestState() {

    }

    override fun initEvent() {
        binding.getSystemServiceButton.setOnClickListener {
            val wifiManager = requireContext().getSystemService<WifiManager>()
            wifiManager?.apply {
                if (ActivityCompat.checkSelfPermission(
                        requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return@setOnClickListener
                }
                val wifiSsidArray = ArrayList<String>()
                scanResults.forEach {
                    wifiSsidArray.add(it.BSSID)
                }

                BottomActionSheet.Builder()
                    .setContext(requireContext())
                    .setActionItemTitle(wifiSsidArray)
                    .setItemTextColor(R.color.mainColor.convertColor(requireContext()))
                    .setOnActionSheetListener(object : BottomActionSheet.OnActionSheetListener {
                        override fun onActionItemClick(position: Int) {
                            wifiSsidArray[position].show(requireContext())
                        }
                    })
                    .build()
                    .show()
            }
        }

        binding.noParamIntentButton.setOnClickListener {
            requireContext().navigatePageTo<ActivityExtensionExample>()
        }

        binding.hasParamIntentButton.setOnClickListener {
            requireContext().navigatePageTo<ActivityExtensionExample>("hasParamIntentButton")
        }

        binding.hasArrayIntentButton.setOnClickListener {
            requireContext().navigatePageTo<ActivityExtensionExample>(
                arrayListOf(
                    "hasParamIntentButton",
                    "hasParamIntentButton",
                    "hasParamIntentButton",
                    "hasParamIntentButton",
                    "hasParamIntentButton"
                )
            )
        }

        binding.showBigImageIntentButton.setOnClickListener {
            requireContext().navigatePageTo<BigImageActivity>(0, imageArray)
        }

        binding.readAssertsButton.setOnClickListener {
            requireContext().readAssetsFile("Test.json").show(requireContext())
        }

        binding.createLogFileButton.setOnClickListener {
            showMessageAlertDialog(requireContext().createLogFile().absolutePath)
        }

        binding.createAudioFileButton.setOnClickListener {
            showMessageAlertDialog(requireContext().createAudioFile().absolutePath)
        }

        binding.createVideoFileButton.setOnClickListener {
            showMessageAlertDialog(requireContext().createVideoFileDir().absolutePath)
        }

        binding.createDownloadDirButton.setOnClickListener {
            showMessageAlertDialog(requireContext().createDownloadFileDir().absolutePath)
        }

        binding.createImageDirButton.setOnClickListener {
            showMessageAlertDialog(requireContext().createImageFileDir().absolutePath)
        }

        binding.createCompressImageDirButton.setOnClickListener {
            showMessageAlertDialog(requireContext().createCompressImageDir().absolutePath)
        }
    }

    private fun showMessageAlertDialog(message: String) {
        AlertDialog.Builder(requireContext()).setTitle("文件路径").setMessage(message).create()
            .show()
    }
}