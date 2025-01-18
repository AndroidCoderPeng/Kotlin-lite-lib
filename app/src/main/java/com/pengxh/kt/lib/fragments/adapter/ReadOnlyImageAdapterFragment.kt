package com.pengxh.kt.lib.fragments.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import com.pengxh.kt.lib.databinding.FragmentAdapterReadonlyImageBinding
import com.pengxh.kt.lite.adapter.GridViewImageAdapter
import com.pengxh.kt.lite.base.KotlinBaseFragment

class ReadOnlyImageAdapterFragment : KotlinBaseFragment<FragmentAdapterReadonlyImageBinding>() {

    private val images = mutableListOf(
        "https://img.zcool.cn/community/010d5c5b9d17c9a8012099c8781b7e.jpg@1280w_1l_2o_100sh.jpg",
        "https://tse4-mm.cn.bing.net/th/id/OIP-C.6szqS1IlGtWsaiHQUtUOVwHaQC?rs=1&pid=ImgDetMain",
        "https://img.zcool.cn/community/01a15855439bdf0000019ae9299cce.jpg@1280w_1l_2o_100sh.jpg",
        "https://pic1.zhimg.com/v2-0cc45f5fda6e8ff79350ec1303835629_r.jpg",
        "https://img.zcool.cn/community/010d5c5b9d17c9a8012099c8781b7e.jpg@1280w_1l_2o_100sh.jpg",
        "https://tse4-mm.cn.bing.net/th/id/OIP-C.6szqS1IlGtWsaiHQUtUOVwHaQC?rs=1&pid=ImgDetMain",
        "https://img.zcool.cn/community/01a15855439bdf0000019ae9299cce.jpg@1280w_1l_2o_100sh.jpg",
        "https://pic1.zhimg.com/v2-0cc45f5fda6e8ff79350ec1303835629_r.jpg"
    )

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAdapterReadonlyImageBinding {
        return FragmentAdapterReadonlyImageBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarLayout() {

    }

    override fun initOnCreate(savedInstanceState: Bundle?) {
        val imageAdapter = GridViewImageAdapter(requireContext(), images)
        binding.gridView.adapter = imageAdapter
        binding.gridView.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, position, l ->

            }
    }

    override fun observeRequestState() {

    }

    override fun initEvent() {

    }
}