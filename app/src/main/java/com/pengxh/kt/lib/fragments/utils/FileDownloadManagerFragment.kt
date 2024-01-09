package com.pengxh.kt.lib.fragments.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.pengxh.kt.lib.databinding.FragmentUtilsFileDownloadBinding
import com.pengxh.kt.lite.base.KotlinBaseFragment
import com.pengxh.kt.lite.extensions.createDownloadFileDir
import com.pengxh.kt.lite.utils.FileDownloadManager
import java.io.File

class FileDownloadManagerFragment : KotlinBaseFragment<FragmentUtilsFileDownloadBinding>() {

    private val kTag = "FileDownloadManagerFragment"

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentUtilsFileDownloadBinding {
        return FragmentUtilsFileDownloadBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarLayout() {

    }

    override fun initOnCreate(savedInstanceState: Bundle?) {

    }

    override fun observeRequestState() {

    }

    override fun initEvent() {
        binding.startDownloadButton.setOnClickListener {
            val text = binding.downloadPathView.text
            if (text.isNullOrBlank()) {
                return@setOnClickListener
            }

            FileDownloadManager.Builder()
                .setDownloadFileSource(text.toString())
                .setFileSuffix(".apk")
                .setFileSaveDirectory(requireContext().createDownloadFileDir())
                .setOnFileDownloadListener(object : FileDownloadManager.OnFileDownloadListener {
                    override fun onProgressChanged(progress: Int) {
                        binding.progressBar.progress = progress
                        binding.startDownloadButton.isEnabled = false
                    }

                    override fun onDownloadEnd(file: File) {
                        binding.filePathView.text = "文件保存路径：${file.absolutePath}"
                        binding.startDownloadButton.isEnabled = true
                    }

                    override fun onFailure(throwable: Throwable) {
                        binding.startDownloadButton.isEnabled = true
                    }
                }).build().start()
        }
    }
}