package com.pengxh.kt.lib.fragments.extensions

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.pengxh.kt.lib.databinding.FragmentExtensionUriBinding
import com.pengxh.kt.lite.base.KotlinBaseFragment
import com.pengxh.kt.lite.extensions.realFilePath
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class UriExtensionFragment : KotlinBaseFragment<FragmentExtensionUriBinding>() {
    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentExtensionUriBinding {
        return FragmentExtensionUriBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarLayout() {

    }

    override fun initOnCreate(savedInstanceState: Bundle?) {

    }

    override fun observeRequestState() {

    }

    override fun initEvent() {
        binding.selectButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            selectImageLauncher.launch(intent)
        }
    }

    private val selectImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
            object : ActivityResultCallback<ActivityResult> {
                override fun onActivityResult(result: ActivityResult) {
                    if (result.resultCode == Activity.RESULT_OK) {
                        val data = result.data ?: return
                        val uri = data.data
                        binding.uriResultView.text = "Uri路径：${uri.toString()}"

                        val realFilePath = uri?.realFilePath(requireContext())
                        binding.pathResultView.text = "实际路径：${realFilePath}"

                        lifecycleScope.launch(Dispatchers.Main) {
                            val originalBitmap = withContext(Dispatchers.IO) {
                                Glide.with(requireContext())
                                    .asBitmap()
                                    .load(realFilePath)
                                    .submit()
                                    .get()
                            }
                            binding.imageView.setImageBitmap(originalBitmap)
                        }
                    }
                }
            }
        )
}