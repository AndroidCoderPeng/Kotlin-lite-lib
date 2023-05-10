package com.pengxh.kt.lib

import android.view.View
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.pengxh.kt.lite.adapter.EditableImageAdapter
import com.pengxh.kt.lite.base.KotlinBaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : KotlinBaseActivity() {

    private val kTag = "MainActivity"
    private val images = listOf(
        "https://images.pexels.com/photos/1036808/pexels-photo-1036808.jpeg",
        "https://images.pexels.com/photos/796602/pexels-photo-796602.jpeg",
        "https://images.pexels.com/photos/1109543/pexels-photo-1109543.jpeg",
        "https://images.pexels.com/photos/296115/pexels-photo-296115.jpeg",
        "https://images.pexels.com/photos/296115/pexels-photo-296115.jpeg",
        "https://images.pexels.com/photos/296115/pexels-photo-296115.jpeg",
        "https://images.pexels.com/photos/296115/pexels-photo-296115.jpeg",
        "https://images.pexels.com/photos/296115/pexels-photo-296115.jpeg",
        "https://images.pexels.com/photos/4158/apple-iphone-smartphone-desk.jpg"
    )
    private val recyclerViewImages = ArrayList<String>()

    override fun initLayoutView(): Int = R.layout.activity_main

    override fun setupTopBarLayout() {

    }

    override fun observeRequestState() {

    }

    override fun initData() {
        val imageAdapter = EditableImageAdapter(this, 9, 2f)
        imageGridView.adapter = imageAdapter
        imageAdapter.setOnItemClickListener(object : EditableImageAdapter.OnItemClickListener {
            override fun onAddImageClick() {
                PictureSelector.create(this@MainActivity)
                    .openGallery(SelectMimeType.ofImage())
                    .isGif(false)
                    .isMaxSelectEnabledMask(true)
                    .setFilterMinFileSize(100)
                    .setMaxSelectNum(9)
                    .isDisplayCamera(false)
                    .setImageEngine(GlideLoadEngine.get)
                    .forResult(object : OnResultCallbackListener<LocalMedia> {
                        override fun onResult(result: ArrayList<LocalMedia>) {
                            result.forEach {
                                recyclerViewImages.add(it.realPath)
                            }
                            imageAdapter.setupImage(recyclerViewImages)
                        }

                        override fun onCancel() {}
                    })
            }

            override fun onItemClick(position: Int) {

            }

            override fun onItemLongClick(view: View?, position: Int) {
                imageAdapter.deleteImage(position)
            }
        })
    }

    override fun initEvent() {

    }
}