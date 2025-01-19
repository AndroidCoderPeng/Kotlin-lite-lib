package com.pengxh.kt.lib.fragments.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.pengxh.kt.lib.R
import com.pengxh.kt.lib.databinding.FragmentWidgetDialogBinding
import com.pengxh.kt.lite.base.KotlinBaseFragment
import com.pengxh.kt.lite.extensions.convertColor
import com.pengxh.kt.lite.extensions.createDownloadFileDir
import com.pengxh.kt.lite.extensions.show
import com.pengxh.kt.lite.extensions.timestampToCompleteDate
import com.pengxh.kt.lite.utils.FileDownloadManager
import com.pengxh.kt.lite.widget.dialog.AlertControlDialog
import com.pengxh.kt.lite.widget.dialog.AlertInputDialog
import com.pengxh.kt.lite.widget.dialog.AlertMessageDialog
import com.pengxh.kt.lite.widget.dialog.BottomActionSheet
import com.pengxh.kt.lite.widget.dialog.ChangePasswordDialog
import com.pengxh.kt.lite.widget.dialog.GlobeAlertDialog
import com.pengxh.kt.lite.widget.dialog.NoNetworkDialog
import com.pengxh.kt.lite.widget.dialog.ProgressDialog
import com.pengxh.kt.lite.widget.dialog.UpdateDialog
import java.io.File

class DialogFragment : KotlinBaseFragment<FragmentWidgetDialogBinding>() {
    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentWidgetDialogBinding {
        return FragmentWidgetDialogBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarLayout() {

    }

    override fun initOnCreate(savedInstanceState: Bundle?) {

    }

    override fun observeRequestState() {

    }

    override fun initEvent() {
        binding.showAlertControlDialogButton.setOnClickListener {
            AlertControlDialog.Builder()
                .setContext(requireContext())
                .setTitle("这里可以是标题")
                .setMessage("这里是对话框显示的内容")
                .setNegativeButton("取消")
                .setPositiveButton("确定")
                .setOnDialogButtonClickListener(object :
                    AlertControlDialog.OnDialogButtonClickListener {
                    override fun onConfirmClick() {

                    }

                    override fun onCancelClick() {

                    }
                })
                .build()
                .show()
        }
        binding.showAlertInputDialogButton.setOnClickListener {
            AlertInputDialog.Builder()
                .setContext(requireContext())
                .setTitle("这里可以是标题")
                .setHintMessage("这里输入要填写的内容")
                .setNegativeButton("取消")
                .setPositiveButton("确定")
                .setOnDialogButtonClickListener(object :
                    AlertInputDialog.OnDialogButtonClickListener {
                    override fun onConfirmClick(value: String) {

                    }

                    override fun onCancelClick() {

                    }
                })
                .build()
                .show()
        }
        binding.showAlertMessageDialogButton.setOnClickListener {
            AlertMessageDialog.Builder()
                .setContext(requireContext())
                .setTitle("这里可以是标题")
                .setMessage("这里是对话框显示的内容")
                .setPositiveButton("确定")
                .setOnDialogButtonClickListener(object :
                    AlertMessageDialog.OnDialogButtonClickListener {
                    override fun onConfirmClick() {

                    }
                })
                .build()
                .show()
        }
        binding.showBottomActionSheetButton.setOnClickListener {
            val array = ArrayList<String>()
            for (i in 0..10) {
                array.add(System.currentTimeMillis().timestampToCompleteDate())
            }

            BottomActionSheet.Builder()
                .setContext(requireContext())
                .setActionItemTitle(array)
                .setItemTextColor(R.color.mainColor.convertColor(requireContext()))
                .setOnActionSheetListener(object : BottomActionSheet.OnActionSheetListener {
                    override fun onActionItemClick(position: Int) {

                    }
                })
                .build()
                .show()
        }
        binding.showChangePasswordDialogButton.setOnClickListener {
            ChangePasswordDialog.Builder()
                .setContext(requireContext())
                .setOnDialogButtonClickListener(object :
                    ChangePasswordDialog.OnDialogButtonClickListener {
                    override fun onConfirmClick(oldPwd: String, newPwd: String) {

                    }
                })
                .build()
                .show()
        }
        binding.showGlobeAlertDialogButton.setOnClickListener {
            GlobeAlertDialog(object : GlobeAlertDialog.OnDialogButtonClickListener {
                override fun onConfirmClick() {

                }

                override fun onCancelClick() {

                }
            }).show(childFragmentManager, this::class.java.simpleName)
        }
        binding.showNoNetworkDialogButton.setOnClickListener {
            NoNetworkDialog.Builder()
                .setContext(requireContext())
                .setOnDialogButtonClickListener(object :
                    NoNetworkDialog.OnDialogButtonClickListener {
                    override fun onButtonClick() {

                    }
                })
                .build()
                .show()
        }
        binding.showProgressDialogButton.setOnClickListener {
            val progressDialog = ProgressDialog(requireContext())
            progressDialog.show()
            FileDownloadManager.Builder()
                .setDownloadFileSource("http://111.198.10.15:20110/apk/2025-01/b33739c02dba389072319b9d5aea95e0.apk")
                .setFileSuffix("apk")
                .setFileSaveDirectory(requireContext().createDownloadFileDir())
                .setOnFileDownloadListener(object : FileDownloadManager.OnFileDownloadListener {
                    override fun onDownloadStart(total: Long) {
                        progressDialog.setMaxProgress(total)
                    }

                    override fun onDownloadEnd(file: File) {
                        progressDialog.dismiss()
                        "下载成功".show(requireContext())
                    }

                    override fun onDownloadFailed(t: Throwable) {
                        t.printStackTrace()
                        progressDialog.dismiss()
                    }

                    override fun onProgressChanged(progress: Long) {
                        progressDialog.updateProgress(progress)
                    }
                }).build().start()
        }
        binding.showUpdateDialogButton.setOnClickListener {
            UpdateDialog.Builder()
                .setContext(requireContext())
                .setUpdateMessage(
                    arrayListOf(
                        "这里是更新内容",
                        "这里是更新内容",
                        "这里是更新内容",
                        "这里是更新内容"
                    )
                )
                .setOnUpdateListener(object : UpdateDialog.OnUpdateListener {
                    override fun onUpdate() {

                    }
                }).build().show()
        }
    }
}