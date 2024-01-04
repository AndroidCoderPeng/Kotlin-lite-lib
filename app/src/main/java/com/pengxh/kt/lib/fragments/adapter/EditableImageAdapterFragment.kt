package com.pengxh.kt.lib.fragments.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.pengxh.kt.lib.databinding.FragmentEditableImageAdapterBinding
import com.pengxh.kt.lib.utils.GlideLoadEngine
import com.pengxh.kt.lite.adapter.EditableImageAdapter
import com.pengxh.kt.lite.base.KotlinBaseFragment

class EditableImageAdapterFragment : KotlinBaseFragment<FragmentEditableImageAdapterBinding>() {

    private lateinit var editableImageAdapter: EditableImageAdapter
    private val recyclerViewImages = ArrayList<String>()

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentEditableImageAdapterBinding {
        return FragmentEditableImageAdapterBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarLayout() {

    }

    override fun initOnCreate(savedInstanceState: Bundle?) {
        editableImageAdapter = EditableImageAdapter(requireContext(), 9, 1)
    }

    override fun observeRequestState() {

    }

    override fun initEvent() {
        binding.recyclerView.adapter = editableImageAdapter
        editableImageAdapter.setOnItemClickListener(object :
            EditableImageAdapter.OnItemClickListener {
            override fun onAddImageClick() {
                selectPicture()
            }

            override fun onItemClick(position: Int) {

            }

            override fun onItemLongClick(view: View?, position: Int) {
                editableImageAdapter.deleteImage(position)
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
            .forResult(object : OnResultCallbackListener<LocalMedia> {
                override fun onResult(result: ArrayList<LocalMedia>) {
                    for (media in result) {
                        recyclerViewImages.add(media.realPath)
                    }
                    editableImageAdapter.setupImage(recyclerViewImages)
                }

                override fun onCancel() {}
            })
    }
}