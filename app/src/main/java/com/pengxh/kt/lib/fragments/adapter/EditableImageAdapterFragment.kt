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
import com.pengxh.kt.lib.databinding.FragmentAdapterEditableImageBinding
import com.pengxh.kt.lib.utils.GlideLoadEngine
import com.pengxh.kt.lite.adapter.EditableImageAdapter
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

class EditableImageAdapterFragment : KotlinBaseFragment<FragmentAdapterEditableImageBinding>(),
    Handler.Callback {

    private lateinit var imageAdapter: EditableImageAdapter
    private val marginOffset by lazy { 1.dp2px(requireContext()) }
    private val weakReferenceHandler by lazy { WeakReferenceHandler(this) }
    private val recyclerViewImages = ArrayList<String>()
    private val selectedImages = ArrayList<LocalMedia>()

    override fun initViewBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentAdapterEditableImageBinding {
        return FragmentAdapterEditableImageBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarLayout() {

    }

    override fun initOnCreate(savedInstanceState: Bundle?) {
        val viewWidth = requireContext().getScreenWidth() - 100.dp2px(requireContext())
        imageAdapter = EditableImageAdapter(requireContext(), viewWidth, 9, 3)
        binding.recyclerView.addItemDecoration(
            RecyclerViewItemOffsets(marginOffset, marginOffset, marginOffset, marginOffset)
        )
        binding.recyclerView.adapter = imageAdapter
    }

    override fun observeRequestState() {

    }

    override fun initEvent() {
        imageAdapter.setOnItemClickListener(object : EditableImageAdapter.OnItemClickListener {
            override fun onAddImageClick() {
                selectPicture()
            }

            override fun onItemClick(position: Int) {

            }

            override fun onItemLongClick(view: View?, position: Int) {
                selectedImages.removeAt(position)
                recyclerViewImages.removeAt(position)
                imageAdapter.notifyImageItemRemoved(recyclerViewImages)
            }
        })
    }

    private fun selectPicture() {
        PictureSelector.create(this).openGallery(SelectMimeType.ofImage()).isGif(false)
            .isMaxSelectEnabledMask(true).setFilterMinFileSize(100).setMaxSelectNum(9)
            .isDisplayCamera(false).setImageEngine(GlideLoadEngine.get)
            .setSelectedData(selectedImages)
            .forResult(object : OnResultCallbackListener<LocalMedia> {
                override fun onResult(result: ArrayList<LocalMedia>) {
                    //重置已选的图片
                    selectedImages.clear()
                    //因为result会带着之前的数据，所以需要清空之前已经add的数据
                    recyclerViewImages.clear()
                    //数据链处理已选的图片
                    lifecycleScope.launch {
                        flow {
                            result.forEach {
                                emit(it)
                                delay(1000)
                            }
                        }.collect {
                            selectedImages.add(it)
                            //压缩图片并上传
                            Luban.with(context).load(it.realPath).ignoreBy(100)
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
                    }


                }

                override fun onCancel() {}
            })
    }

    override fun handleMessage(msg: Message): Boolean {
        if (msg.what == 2024042301) {
            val file = msg.obj as File

            recyclerViewImages.add(file.absolutePath)
            imageAdapter.notifyImageItemRangeInserted(recyclerViewImages)
        }
        return true
    }
}