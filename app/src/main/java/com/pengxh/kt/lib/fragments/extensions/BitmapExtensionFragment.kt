package com.pengxh.kt.lib.fragments.extensions

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.lifecycleScope
import com.pengxh.kt.lib.R
import com.pengxh.kt.lib.databinding.FragmentExtensionBitmapBinding
import com.pengxh.kt.lite.base.KotlinBaseFragment
import com.pengxh.kt.lite.extensions.createRoundDrawable
import com.pengxh.kt.lite.extensions.dp2px
import com.pengxh.kt.lite.extensions.getScreenWidth
import com.pengxh.kt.lite.extensions.rotateImage
import com.pengxh.kt.lite.extensions.toBase64
import com.pengxh.kt.lite.extensions.writeToFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BitmapExtensionFragment : KotlinBaseFragment<FragmentExtensionBitmapBinding>() {

    private lateinit var originalBitmap: Bitmap
    private lateinit var base64File: File

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentExtensionBitmapBinding {
        return FragmentExtensionBitmapBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarLayout() {

    }

    override fun initOnCreate(savedInstanceState: Bundle?) {
        val viewWidth = requireContext().getScreenWidth() - 20.dp2px(requireContext())
        val imageSize = viewWidth / 2
        val params = LinearLayout.LayoutParams(imageSize, imageSize)
        binding.originalImageView.layoutParams = params
        binding.revolvedImageView.layoutParams = params

        lifecycleScope.launch(Dispatchers.Main) {
            originalBitmap = withContext(Dispatchers.IO) {
                BitmapFactory.decodeResource(resources, R.mipmap.test)
            }
            binding.originalImageView.setImageBitmap(originalBitmap)
        }
        base64File = createBase64File()
    }

    override fun observeRequestState() {

    }

    override fun initEvent() {
        binding.rotateButton.setOnClickListener {
            if (binding.angleView.text.isNullOrBlank()) {
                return@setOnClickListener
            }
            val rotateImage =
                originalBitmap.rotateImage(binding.angleView.text.toString().toFloat())
            binding.revolvedImageView.setImageBitmap(rotateImage)
        }

        binding.base64Button.setOnClickListener {
            val base64 = originalBitmap.toBase64()
            base64.writeToFile(base64File)

            binding.base64View.text = "Base64编码文件路径：${base64File.absolutePath}"
        }

        binding.createRoundImageButton.setOnClickListener {
            val roundDrawable = originalBitmap.createRoundDrawable(
                requireContext(), 5f, Color.RED
            )
            binding.roundImageView.setImageBitmap(roundDrawable)
        }
    }

    private fun createBase64File(): File {
        val documentDir = File(
            requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), ""
        )
        val timeStamp = SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA).format(Date())
        val file = File(documentDir.toString() + File.separator + "base64_" + timeStamp + ".txt")
        if (!file.exists()) {
            try {
                file.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return file
    }
}

