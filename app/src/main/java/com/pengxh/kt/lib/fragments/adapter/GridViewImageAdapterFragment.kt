package com.pengxh.kt.lib.fragments.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.pengxh.kt.lib.databinding.FragmentAdapterGridviewImageBinding
import com.pengxh.kt.lite.adapter.GridViewImageAdapter
import com.pengxh.kt.lite.base.KotlinBaseFragment

class GridViewImageAdapterFragment : KotlinBaseFragment<FragmentAdapterGridviewImageBinding>() {

    private val images = mutableListOf(
        "https://pic1.zhimg.com/v2-0cc45f5fda6e8ff79350ec1303835629_r.jpg",
        "https://pic1.zhimg.com/v2-0cc45f5fda6e8ff79350ec1303835629_r.jpg",
        "https://pic1.zhimg.com/v2-0cc45f5fda6e8ff79350ec1303835629_r.jpg",
        "https://pic1.zhimg.com/v2-0cc45f5fda6e8ff79350ec1303835629_r.jpg",
        "https://pic1.zhimg.com/v2-0cc45f5fda6e8ff79350ec1303835629_r.jpg",
        "https://pic1.zhimg.com/v2-0cc45f5fda6e8ff79350ec1303835629_r.jpg",
        "https://pic1.zhimg.com/v2-0cc45f5fda6e8ff79350ec1303835629_r.jpg",
        "https://pic1.zhimg.com/v2-0cc45f5fda6e8ff79350ec1303835629_r.jpg",
        "https://pic1.zhimg.com/v2-0cc45f5fda6e8ff79350ec1303835629_r.jpg",
        "https://pic1.zhimg.com/v2-0cc45f5fda6e8ff79350ec1303835629_r.jpg"
    )

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAdapterGridviewImageBinding {
        return FragmentAdapterGridviewImageBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarLayout() {

    }

    override fun initOnCreate(savedInstanceState: Bundle?) {
        binding.gridView.adapter = GridViewImageAdapter(requireContext(), images)
    }

    override fun observeRequestState() {

    }

    override fun initEvent() {

    }
}