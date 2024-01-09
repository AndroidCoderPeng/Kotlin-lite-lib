package com.pengxh.kt.lib.fragments.utils

import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioButton
import com.bumptech.glide.Glide
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.pengxh.kt.lib.databinding.FragmentUtilsWaterMarkerBinding
import com.pengxh.kt.lib.utils.GlideLoadEngine
import com.pengxh.kt.lib.view.BigImageActivity
import com.pengxh.kt.lite.annotations.WaterMarkPosition
import com.pengxh.kt.lite.base.KotlinBaseFragment
import com.pengxh.kt.lite.extensions.createCompressImageDir
import com.pengxh.kt.lite.extensions.dp2px
import com.pengxh.kt.lite.extensions.navigatePageTo
import com.pengxh.kt.lite.extensions.show
import com.pengxh.kt.lite.extensions.sp2px
import com.pengxh.kt.lite.extensions.timestampToCompleteDate
import com.pengxh.kt.lite.utils.LoadingDialogHub
import com.pengxh.kt.lite.utils.WaterMarkerEngine
import java.io.File

class WaterMarkerEngineFragment : KotlinBaseFragment<FragmentUtilsWaterMarkerBinding>() {

    private var mediaRealPath: String? = null
    private lateinit var compressImageDir: File

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentUtilsWaterMarkerBinding {
        return FragmentUtilsWaterMarkerBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarLayout() {

    }

    override fun initOnCreate(savedInstanceState: Bundle?) {
        compressImageDir = requireContext().createCompressImageDir()

        val radioButton =
            binding.radioGroup.getChildAt(binding.radioGroup.childCount - 2) as RadioButton
        radioButton.isChecked = true
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

            var checkedValue = "右下"
            for (i in 0 until binding.radioGroup.childCount) {
                val radioButton = binding.radioGroup.getChildAt(i) as RadioButton
                if (radioButton.isChecked) {
                    checkedValue = radioButton.text.toString()
                }
            }

            val position = when (checkedValue) {
                "左上" -> WaterMarkPosition.LEFT_TOP
                "左下" -> WaterMarkPosition.LEFT_BOTTOM
                "右上" -> WaterMarkPosition.RIGHT_TOP
                "右下" -> WaterMarkPosition.RIGHT_BOTTOM
                else -> WaterMarkPosition.CENTER
            }


            val bitmap = BitmapFactory.decodeFile(mediaRealPath)
            WaterMarkerEngine.Builder()
                .setOriginalBitmap(bitmap)
                .setTextMaker(System.currentTimeMillis().timestampToCompleteDate())
                .setTextColor(Color.RED)
                .setTextSize(50f.sp2px(requireContext()))
                .setMarkerPosition(position)
                .setTextMargin(30f.dp2px(requireContext()))
                .setMarkedSavePath("${compressImageDir}/${System.currentTimeMillis()}.png")
                .setOnWaterMarkerAddedListener(object :
                    WaterMarkerEngine.OnWaterMarkerAddedListener {
                    override fun onStart() {
                        LoadingDialogHub.show(requireActivity(), "水印添加中，请稍后...")
                    }

                    override fun onMarkAdded(file: File) {
                        Glide.with(requireContext())
                            .load(file)
                            .into(binding.markerImageView)

                        LoadingDialogHub.dismiss()
                    }
                }).build().start()
        }
    }
}