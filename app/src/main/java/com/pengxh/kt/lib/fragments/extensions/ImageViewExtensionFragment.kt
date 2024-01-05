package com.pengxh.kt.lib.fragments.extensions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.pengxh.kt.lib.databinding.FragmentExtensionImageViewBinding
import com.pengxh.kt.lite.base.KotlinBaseFragment
import com.pengxh.kt.lite.extensions.switchBackground
import com.pengxh.kt.lite.extensions.toBlurBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ImageViewExtensionFragment : KotlinBaseFragment<FragmentExtensionImageViewBinding>() {
    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentExtensionImageViewBinding {
        return FragmentExtensionImageViewBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarLayout() {

    }

    override fun initOnCreate(savedInstanceState: Bundle?) {
        lifecycleScope.launch(Dispatchers.Main) {
            val drawable = withContext(Dispatchers.IO) {
                Glide.with(requireContext())
                    .load("https://pic1.zhimg.com/v2-0cc45f5fda6e8ff79350ec1303835629_r.jpg")
                    .submit()
                    .get()
            }
            binding.blurImageView.setImageDrawable(drawable)
        }
        binding.blurImageView.postDelayed(blurRunnable, 500)
    }

    private val blurRunnable = Runnable {
        lifecycleScope.launch(Dispatchers.Main) {
            try {
                val drawable = withContext(Dispatchers.IO) {
                    Glide.with(requireContext())
                        .load("https://pic1.zhimg.com/v2-0cc45f5fda6e8ff79350ec1303835629_r.jpg")
                        .submit()
                        .get()
                }
                binding.blurImageView.switchBackground(
                    drawable.toBlurBitmap(requireContext(), 20f)
                )
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            }
        }
    }

    override fun observeRequestState() {

    }

    override fun initEvent() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.blurImageView.removeCallbacks(blurRunnable)
    }
}