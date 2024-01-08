package com.pengxh.kt.lib.fragments.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.pengxh.kt.lib.databinding.FragmentUtilsSaveKeyValuesBinding
import com.pengxh.kt.lite.base.KotlinBaseFragment
import com.pengxh.kt.lite.utils.SaveKeyValues

class SaveKeyValuesFragment : KotlinBaseFragment<FragmentUtilsSaveKeyValuesBinding>() {

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentUtilsSaveKeyValuesBinding {
        return FragmentUtilsSaveKeyValuesBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarLayout() {

    }

    override fun initOnCreate(savedInstanceState: Bundle?) {
        SaveKeyValues.initSharedPreferences(requireContext())
    }

    override fun observeRequestState() {

    }

    override fun initEvent() {
        /**
         * 存取数据一定要注意类型统一，否则会报错——[ClassCastException]
         *
         * 如存String
         * 取Int
         * ——java.lang.ClassCastException: java.lang.String cannot be cast to java.lang.Integer
         * */
        binding.saveValueButton.setOnClickListener {
            SaveKeyValues.putValue("SaveKeyValuesFragment", binding.inputSpView.text.toString())
        }

        binding.getValueButton.setOnClickListener {
            val value = SaveKeyValues.getValue("SaveKeyValuesFragment", "") as String
            binding.showSpView.text = value
        }
    }
}