package com.pengxh.kt.lib

import android.graphics.Color
import android.view.View
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pengxh.kt.lite.adapter.NormalRecyclerAdapter
import com.pengxh.kt.lite.adapter.ViewHolder
import com.pengxh.kt.lite.base.KotlinBaseActivity
import com.pengxh.kt.lite.extensions.readAssetsFile
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : KotlinBaseActivity() {

    private val kTag = "MainActivity"
    private val gson by lazy { Gson() }

    override fun initLayoutView(): Int = R.layout.activity_main

    override fun setupTopBarLayout() {

    }

    override fun observeRequestState() {

    }

    override fun initData() {
        val data = readAssetsFile("TestData.json")
        val models = gson.fromJson<TestDataModel>(
            data, object : TypeToken<TestDataModel>() {}.type
        )

        val logAdapter = object : NormalRecyclerAdapter<TestDataModel.DataModel>(
            R.layout.item_entrust_log_rv_l, models.data
        ) {
            override fun convertView(
                viewHolder: ViewHolder,
                position: Int,
                item: TestDataModel.DataModel
            ) {
                when (position) {
                    0 -> {
                        //最后一项
                        viewHolder.setBackgroundColor(R.id.dotView, Color.BLACK)
                        viewHolder.setTextColor(R.id.operatorNameView, Color.BLACK)
                            .setTextColor(R.id.statusView, Color.BLACK)
                            .setTextColor(R.id.operateTimeView, Color.BLACK)
                            .setTextColor(R.id.remarkView, Color.BLACK)

                        viewHolder.setVisibility(R.id.topLineView, View.INVISIBLE)
                        viewHolder.setImageResource(R.id.tagImageView, R.drawable.dot_top)
                    }
                    models.data.size - 1 -> {
                        viewHolder.setVisibility(R.id.bottomLineView, View.INVISIBLE)
                        viewHolder.setImageResource(R.id.tagImageView, R.drawable.dot_bottom)
                    }
                    else -> {
                        viewHolder.setImageResource(R.id.tagImageView, R.drawable.dot_middle)
                    }
                }

                viewHolder.setText(R.id.operatorNameView, item.createUserName)
                    .setText(R.id.statusView, item.status)
                    .setText(R.id.operateTimeView, item.createTime)
                    .setText(R.id.remarkView, item.recordContent)
            }
        }
        recyclerView.adapter = logAdapter
    }

    override fun initEvent() {

    }
}