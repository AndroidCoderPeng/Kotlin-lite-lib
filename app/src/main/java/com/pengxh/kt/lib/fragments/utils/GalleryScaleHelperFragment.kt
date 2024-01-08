package com.pengxh.kt.lib.fragments.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pengxh.kt.lib.R
import com.pengxh.kt.lib.databinding.FragmentUtilsGalleryScaleBinding
import com.pengxh.kt.lite.adapter.NormalRecyclerAdapter
import com.pengxh.kt.lite.adapter.ViewHolder
import com.pengxh.kt.lite.base.KotlinBaseFragment
import com.pengxh.kt.lite.extensions.switchBackground
import com.pengxh.kt.lite.extensions.toBlurBitmap
import com.pengxh.kt.lite.utils.GalleryScaleHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GalleryScaleHelperFragment : KotlinBaseFragment<FragmentUtilsGalleryScaleBinding>() {

    private val imageArray = mutableListOf(
        "https://img.zcool.cn/community/010d5c5b9d17c9a8012099c8781b7e.jpg@1280w_1l_2o_100sh.jpg",
        "https://tse4-mm.cn.bing.net/th/id/OIP-C.6szqS1IlGtWsaiHQUtUOVwHaQC?rs=1&pid=ImgDetMain",
        "https://img.zcool.cn/community/01a15855439bdf0000019ae9299cce.jpg@1280w_1l_2o_100sh.jpg",
        "https://pic1.zhimg.com/v2-0cc45f5fda6e8ff79350ec1303835629_r.jpg"
    )
    private val scaleHelper by lazy { GalleryScaleHelper() }
    private var blurRunnable: Runnable? = null

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentUtilsGalleryScaleBinding {
        return FragmentUtilsGalleryScaleBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarLayout() {

    }

    override fun initOnCreate(savedInstanceState: Bundle?) {
        val galleryAdapter = object : NormalRecyclerAdapter<String>(
            R.layout.item_gallery_rv_l, imageArray
        ) {
            override fun convertView(viewHolder: ViewHolder, position: Int, item: String) {
                lifecycleScope.launch(Dispatchers.Main) {
                    val drawable = withContext(Dispatchers.IO) {
                        Glide.with(requireContext())
                            .load(item)
                            .submit()
                            .get()
                    }
                    viewHolder.setImageResource(R.id.imageView, drawable)
                }
            }
        }
        binding.recyclerView.adapter = galleryAdapter
        scaleHelper.attachToRecyclerView(binding.recyclerView)
        renderBackground(scaleHelper.getCurrentIndex())
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                renderBackground(scaleHelper.getCurrentIndex())
            }
        })
    }

    override fun observeRequestState() {

    }

    override fun initEvent() {

    }

    /**
     * 渲染高斯模糊背景
     * */
    private fun renderBackground(index: Int) {
        blurRunnable?.apply {
            binding.blurImageView.removeCallbacks(this)
        }
        blurRunnable = Runnable {
            lifecycleScope.launch(Dispatchers.Main) {
                val drawable = withContext(Dispatchers.IO) {
                    Glide.with(requireContext())
                        .load(imageArray[index])
                        .submit()
                        .get()
                }
                binding.blurImageView.switchBackground(
                    drawable.toBlurBitmap(requireContext(), 20f)
                )
            }
        }
        binding.blurImageView.postDelayed(blurRunnable, 500)
    }
}