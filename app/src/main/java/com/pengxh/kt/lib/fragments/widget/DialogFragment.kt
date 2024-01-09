package com.pengxh.kt.lib.fragments.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.pengxh.kt.lib.R
import com.pengxh.kt.lib.databinding.FragmentWidgetDialogBinding
import com.pengxh.kt.lite.base.KotlinBaseFragment
import com.pengxh.kt.lite.extensions.convertColor
import com.pengxh.kt.lite.extensions.timestampToCompleteDate
import com.pengxh.kt.lite.widget.dialog.AlertControlDialog
import com.pengxh.kt.lite.widget.dialog.AlertInputDialog
import com.pengxh.kt.lite.widget.dialog.AlertMessageDialog
import com.pengxh.kt.lite.widget.dialog.BottomActionSheet
import com.pengxh.kt.lite.widget.dialog.ChangePasswordDialog
import com.pengxh.kt.lite.widget.dialog.GlobeAlertDialog
import com.pengxh.kt.lite.widget.dialog.NoNetworkDialog

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
    }
}