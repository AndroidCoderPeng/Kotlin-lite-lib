package com.pengxh.kt.lib.fragments.extensions

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.pengxh.kt.lib.databinding.FragmentExtensionDrawableBinding
import com.pengxh.kt.lite.base.KotlinBaseFragment
import com.pengxh.kt.lite.extensions.toBlurBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 更具自身Drawable生成高斯模糊的Bitmap
 * */
class DrawableExtensionFragment : KotlinBaseFragment<FragmentExtensionDrawableBinding>() {

    private val imageUrl = "https://pic1.zhimg.com/v2-0cc45f5fda6e8ff79350ec1303835629_r.jpg"
    private lateinit var originalDrawable: Drawable

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentExtensionDrawableBinding {
        return FragmentExtensionDrawableBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarLayout() {

    }

    override fun initOnCreate(savedInstanceState: Bundle?) {
        lifecycleScope.launch(Dispatchers.Main) {
            originalDrawable = withContext(Dispatchers.IO) {
                Glide.with(requireContext())
                    .load(imageUrl)
                    .submit()
                    .get()
            }
            binding.originalImageView.setImageDrawable(originalDrawable)
        }
    }

    override fun observeRequestState() {

    }

    override fun initEvent() {
        binding.gaussianBlurButton.setOnClickListener {
            binding.blurImageView.setImageBitmap(
                originalDrawable.toBlurBitmap(requireContext(), 20f)
            )
        }
    }
}