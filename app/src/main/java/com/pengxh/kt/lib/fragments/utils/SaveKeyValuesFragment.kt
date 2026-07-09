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
        binding.saveValueButton.setOnClickListener {
            SaveKeyValues.putString("SaveKeyValuesFragment", binding.inputSpView.text.toString())
        }

        binding.getValueButton.setOnClickListener {
            val value = SaveKeyValues.getString("SaveKeyValuesFragment", "")
            binding.showSpView.text = value
        }
    }
}