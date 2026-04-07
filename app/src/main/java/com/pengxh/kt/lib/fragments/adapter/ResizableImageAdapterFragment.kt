package com.pengxh.kt.lib.fragments.adapter

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.pengxh.kt.lib.databinding.FragmentAdapterResizableImageBinding
import com.pengxh.kt.lib.utils.GlideLoadEngine
import com.pengxh.kt.lite.adapter.ResizableImageAdapter
import com.pengxh.kt.lite.base.KotlinBaseFragment
import com.pengxh.kt.lite.divider.RecyclerViewItemOffsets
import com.pengxh.kt.lite.extensions.createCompressImageDir
import com.pengxh.kt.lite.extensions.dp2px
import com.pengxh.kt.lite.extensions.getScreenWidth
import com.pengxh.kt.lite.utils.WeakReferenceHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.File

class ResizableImageAdapterFragment : KotlinBaseFragment<FragmentAdapterResizableImageBinding>(),
    Handler.Callback {

    private lateinit var imageAdapter: ResizableImageAdapter
    private val marginOffset by lazy { 1.dp2px(requireContext()) }
    private val weakReferenceHandler by lazy { WeakReferenceHandler(this) }
    private val selectedImages = ArrayList<LocalMedia>()

    override fun initViewBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentAdapterResizableImageBinding {
        return FragmentAdapterResizableImageBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarLayout() {

    }

    override fun initOnCreate(savedInstanceState: Bundle?) {
        val viewWidth = requireContext().getScreenWidth() - 20.dp2px(requireContext())
        imageAdapter = ResizableImageAdapter(mutableListOf(), viewWidth)
        binding.recyclerView.addItemDecoration(
            RecyclerViewItemOffsets(marginOffset, marginOffset, marginOffset, marginOffset)
        )
        binding.recyclerView.adapter = imageAdapter
    }

    override fun observeRequestState() {

    }

    override fun initEvent() {
        imageAdapter.setOnItemClickListener(object : ResizableImageAdapter.OnItemClickListener {
            override fun onAddImageClick() {
                selectPicture()
            }

            override fun onItemClick(position: Int) {

            }

            override fun onItemLongClick(view: View?, position: Int) {
                selectedImages.removeAt(position)
                imageAdapter.removeItem(position)
            }
        })
    }

    private fun selectPicture() {
        PictureSelector.create(this)
            .openGallery(SelectMimeType.ofImage())
            .isGif(false)
            .isMaxSelectEnabledMask(true)
            .setFilterMinFileSize(100)
            .setMaxSelectNum(9)
            .isDisplayCamera(false)
            .setImageEngine(GlideLoadEngine.get)
            .setSelectedData(selectedImages)
            .forResult(object : OnResultCallbackListener<LocalMedia> {
                override fun onResult(result: ArrayList<LocalMedia>) {
                    lifecycleScope.launch {
                        flow {
                            result.forEach {
                                emit(it)
                            }
                        }.collect { image ->
                            // 对比现有的图片和新选的图片，只新增不存在的图片
                            val isSelected = selectedImages.any { it.realPath == image.realPath }
                            if (!isSelected) {
                                selectedImages.add(image)

                                // 压缩图片并上传
                                zipImage(image)

                                // 控制处理频率，避免并发过多
                                delay(1000)
                            }
                        }
                    }
                }

                override fun onCancel() {}
            })
    }

    private fun zipImage(image: LocalMedia) {
        Luban.with(requireContext())
            .load(image.realPath)
            .ignoreBy(100)
            .setTargetDir(requireContext().createCompressImageDir().toString())
            .setCompressListener(object : OnCompressListener {
                override fun onStart() {

                }

                override fun onSuccess(file: File) {
                    //模拟上传图片
                    val message = weakReferenceHandler.obtainMessage()
                    message.what = 2024042301
                    message.obj = file
                    weakReferenceHandler.sendMessageDelayed(message, 500)
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }
            }).launch()
    }

    override fun handleMessage(msg: Message): Boolean {
        if (msg.what == 2024042301) {
            val file = msg.obj as File
            imageAdapter.addItem(file.absolutePath)
        }
        return true
    }
}