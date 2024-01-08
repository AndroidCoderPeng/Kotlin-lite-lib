package com.pengxh.kt.lib.fragments.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.pengxh.kt.lib.databinding.FragmentUtilsLoadingDialogHubBinding
import com.pengxh.kt.lite.base.KotlinBaseFragment
import com.pengxh.kt.lite.utils.LoadingDialogHub
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoadingDialogFragment : KotlinBaseFragment<FragmentUtilsLoadingDialogHubBinding>() {
    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentUtilsLoadingDialogHubBinding {
        return FragmentUtilsLoadingDialogHubBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarLayout() {

    }

    override fun initOnCreate(savedInstanceState: Bundle?) {

    }

    override fun observeRequestState() {

    }

    override fun initEvent() {
        binding.showButton.setOnClickListener {
            lifecycleScope.launch(Dispatchers.Main) {
                LoadingDialogHub.show(requireActivity(), "不带标题的加载框")
                delay(3000)
                LoadingDialogHub.dismiss()
            }
        }

        binding.showWithTitleButton.setOnClickListener {
            lifecycleScope.launch(Dispatchers.Main) {
                LoadingDialogHub.show(requireActivity(), "标题", "带标题的加载框")
                delay(3000)
                LoadingDialogHub.dismiss()
            }
        }
    }
}