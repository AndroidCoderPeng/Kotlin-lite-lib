package com.pengxh.kt.lib.fragments.extensions

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioButton
import com.pengxh.kt.lib.databinding.FragmentExtensionDialogBinding
import com.pengxh.kt.lib.example.DialogExample
import com.pengxh.kt.lite.base.KotlinBaseFragment

/**
 * 对话框扩展函数
 *
 * 1、根据实际情况设置dialog占屏幕宽度比例，默认0.75f
 *
 * 2、设置自定义dialog出现/消失的动画
 *
 * 3、设置dialog出现的位置，默认居中
 * */
class DialogExtensionFragment : KotlinBaseFragment<FragmentExtensionDialogBinding>() {

    private val kTag = "DialogExtensionFragment"

    //dialog默认宽度占比
    private var ratio = 0.75f

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentExtensionDialogBinding {
        return FragmentExtensionDialogBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarLayout() {

    }

    override fun initOnCreate(savedInstanceState: Bundle?) {
        val radioButton =
            binding.radioGroup.getChildAt(binding.radioGroup.childCount - 1) as RadioButton
        radioButton.isChecked = true
    }

    override fun observeRequestState() {

    }

    override fun initEvent() {
        binding.showDialogButton.setOnClickListener {
            if (!binding.inputView.text.isNullOrBlank()) {
                ratio = binding.inputView.text.toString().toFloat()
            }

            var checkedValue = "居中"
            for (i in 0 until binding.radioGroup.childCount) {
                val radioButton = binding.radioGroup.getChildAt(i) as RadioButton
                if (radioButton.isChecked) {
                    checkedValue = radioButton.text.toString()
                }
            }

            val gravity = when (checkedValue) {
                "靠上" -> Gravity.TOP
                "靠下" -> Gravity.BOTTOM
                "靠左" -> Gravity.START
                "靠右" -> Gravity.END
                else -> Gravity.CENTER
            }

            DialogExample(requireContext(), ratio, gravity).show()
        }
    }
}