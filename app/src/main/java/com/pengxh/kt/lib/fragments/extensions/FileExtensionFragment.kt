package com.pengxh.kt.lib.fragments.extensions

import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.ViewGroup
import com.pengxh.kt.lib.databinding.FragmentExtensionFileBinding
import com.pengxh.kt.lite.base.KotlinBaseFragment
import com.pengxh.kt.lite.extensions.calculateSize
import com.pengxh.kt.lite.extensions.deleteFile
import com.pengxh.kt.lite.extensions.formatFileSize
import com.pengxh.kt.lite.extensions.read
import com.pengxh.kt.lite.extensions.toBase64
import com.pengxh.kt.lite.extensions.writeToFile
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FileExtensionFragment : KotlinBaseFragment<FragmentExtensionFileBinding>() {

    private lateinit var documentDir: File
    private lateinit var imageFilePath: String
    private lateinit var base64File: File
    private lateinit var targetDir: File

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentExtensionFileBinding {
        return FragmentExtensionFileBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarLayout() {

    }

    override fun initOnCreate(savedInstanceState: Bundle?) {
        documentDir = File(
            requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), ""
        )
        binding.filePathView.text = "$documentDir${File.separator}协议模拟数据.txt"

        val imageDir = File(
            requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), ""
        )
        imageFilePath = "$imageDir${File.separator}加密.png"
        binding.originalImageView.setImageBitmap(BitmapFactory.decodeFile(imageFilePath))

        base64File = createBase64File()

        binding.dirPathView.text = "目标路径：$documentDir"

        targetDir = File(
            requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Test"
        )
        binding.targetPathView.text = "目标路径：$targetDir"
        binding.beforeDeleteSizeView.text = "删除前：${targetDir.calculateSize().formatFileSize()}"
    }

    override fun observeRequestState() {

    }

    override fun initEvent() {
        binding.readFileButton.setOnClickListener {
            binding.fileContentView.text = File(binding.filePathView.text.toString()).read()
        }

        binding.base64Button.setOnClickListener {
            val imageFile = File(imageFilePath).toBase64()
            imageFile.writeToFile(base64File)

            binding.base64View.text = "Base64编码文件路径：${base64File.absolutePath}"
        }

        binding.calculateSizeButton.setOnClickListener {
            binding.dirSizeView.text = "${documentDir.calculateSize()}"
        }

        binding.deleteButton.setOnClickListener {
            targetDir.deleteFile()
            binding.afterDeleteSizeView.text =
                "删除后：${targetDir.calculateSize().formatFileSize()}"
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