package com.pengxh.kt.lib.fragments.utils

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.pengxh.kt.lib.databinding.FragmentUtilsOverlayBinding
import com.pengxh.kt.lib.utils.GlideLoadEngine
import com.pengxh.kt.lib.view.BigImageActivity
import com.pengxh.kt.lite.base.KotlinBaseFragment
import com.pengxh.kt.lite.extensions.createCompressImageDir
import com.pengxh.kt.lite.extensions.navigatePageTo
import com.pengxh.kt.lite.extensions.show
import com.pengxh.kt.lite.extensions.timestampToCompleteDate
import com.pengxh.kt.lite.utils.LoadingDialog
import com.pengxh.kt.lite.utils.OverlayCreator
import java.io.File

class OverlayCreatorFragment : KotlinBaseFragment<FragmentUtilsOverlayBinding>() {

    private var mediaRealPath: String? = null
    private lateinit var compressImageDir: File

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentUtilsOverlayBinding {
        return FragmentUtilsOverlayBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarLayout() {

    }

    override fun initOnCreate(savedInstanceState: Bundle?) {
        compressImageDir = requireContext().createCompressImageDir()
    }

    override fun observeRequestState() {

    }

    override fun initEvent() {
        binding.selectImageButton.setOnClickListener {
            PictureSelector.create(this)
                .openGallery(SelectMimeType.ofImage())
                .isGif(false)
                .isMaxSelectEnabledMask(true)
                .setFilterMinFileSize(100)
                .setMaxSelectNum(1)
                .isDisplayCamera(false)
                .setImageEngine(GlideLoadEngine.get)
                .forResult(object : OnResultCallbackListener<LocalMedia> {
                    override fun onResult(result: ArrayList<LocalMedia>?) {
                        if (result == null) {
                            "选择照片失败，请重试".show(requireContext())
                            return
                        }

                        val media = result[0]
                        mediaRealPath = media.realPath

                        Glide.with(requireContext())
                            .load(mediaRealPath)
                            .into(binding.originalImageView)

                        binding.originalImageView.setOnClickListener {
                            val urls = ArrayList<String>()
                            urls.add(mediaRealPath!!)
                            requireContext().navigatePageTo<BigImageActivity>(0, urls)
                        }
                    }

                    override fun onCancel() {}
                })
        }

        binding.addMarkerButton.setOnClickListener {
            if (mediaRealPath == null) {
                "请先选择图片再添加水印".show(requireContext())
                return@setOnClickListener
            }

            val lines = listOf(
                "北京市海淀区永定路52号",
                System.currentTimeMillis().timestampToCompleteDate()
            )

            val bitmap = BitmapFactory.decodeFile(mediaRealPath)
            OverlayCreator.Builder()
                .setContext(requireContext())
                .setOriginalBitmap(bitmap)
                .setOverlay(lines)
                .setPosition(Pair(0.75f, 0.75f))
                .setOutputPath("${compressImageDir}/${System.currentTimeMillis()}.png")
                .setOnStateChangedListener(object : OverlayCreator.OnStateChangedListener {
                    override fun onStart() {
                        LoadingDialog.show(requireActivity(), "水印添加中，请稍后...")
                    }

                    override fun onSuccess(file: File) {
                        Glide.with(requireContext())
                            .load(file)
                            .into(binding.markerImageView)

                        LoadingDialog.dismiss()
                    }

                    override fun onFailure(e: Exception) {
                        LoadingDialog.dismiss()
                        "添加水印失败".show(requireContext())
                    }
                }).build().start()
        }
    }
}