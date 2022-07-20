package com.pengxh.kt.lib

import com.pengxh.kt.lite.adapter.EditableImageAdapter
import com.pengxh.kt.lite.adapter.ReadOnlyImageAdapter
import com.pengxh.kt.lite.base.KotlinBaseActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : KotlinBaseActivity() {

    private val kTag = "MainActivity"
    private val images = arrayListOf(
        "https://images.pexels.com/photos/3052361/pexels-photo-3052361.jpeg",
        "https://images.pexels.com/photos/3565742/pexels-photo-3565742.jpeg",
        "https://images.pexels.com/photos/2931915/pexels-photo-2931915.jpeg",
        "https://images.pexels.com/photos/6592658/pexels-photo-6592658.jpeg",
        "https://images.pexels.com/photos/5611139/pexels-photo-5611139.jpeg",
        "https://images.pexels.com/photos/2931915/pexels-photo-2931915.jpeg",
        "https://images.pexels.com/photos/6592658/pexels-photo-6592658.jpeg",
        "https://images.pexels.com/photos/5611139/pexels-photo-5611139.jpeg",
        "https://images.pexels.com/photos/6612350/pexels-photo-6612350.jpeg"
    )

    override fun initLayoutView(): Int = R.layout.activity_main

    override fun setupTopBarLayout() {

    }

    override fun initData() {
        val readOnlyImageAdapter = ReadOnlyImageAdapter(this)
        readOnlyImageAdapter.setImageList(images)
        readonlyGridView.adapter = readOnlyImageAdapter
    }

    override fun initEvent() {
        val editableImageAdapter = EditableImageAdapter(this, 9, 3f)
        editableImageAdapter.setupImage(images)
        editableGridView.adapter = editableImageAdapter
    }
}