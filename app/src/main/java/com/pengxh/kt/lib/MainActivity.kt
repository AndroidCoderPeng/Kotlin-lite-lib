package com.pengxh.kt.lib

import android.util.Log
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pengxh.kt.lite.adapter.MultipleChoiceAdapter
import com.pengxh.kt.lite.adapter.ViewHolder
import com.pengxh.kt.lite.base.KotlinBaseActivity
import com.pengxh.kt.lite.extensions.readAssetsFile
import com.pengxh.kt.lite.extensions.toJson
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : KotlinBaseActivity() {

    private val kTag = "MainActivity"
    private val gson by lazy { Gson() }
    private var models: List<SampleListModel.DataModel.RowsModel> = ArrayList()

    override fun initLayoutView(): Int = R.layout.activity_main

    override fun setupTopBarLayout() {

    }

    override fun observeRequestState() {

    }

    override fun initData() {
        val data = readAssetsFile("TestData.json")
        models = gson.fromJson<SampleListModel>(
            data, object : TypeToken<SampleListModel>() {}.type
        ).data.rows
    }

    override fun initEvent() {
        /**
         * 普通列表
         * */
//        val normalRecyclerAdapter = object : NormalRecyclerAdapter<SampleListModel.DataModel.RowsModel>(
//            R.layout.item_select_sample_lv, models
//        ) {
//            override fun convertView(
//                viewHolder: ViewHolder, position: Int, item: SampleListModel.DataModel.RowsModel
//            ) {
//                viewHolder.setText(R.id.sampleNameView, "${item.sampleName}【${item.sampleModel}】")
//                    .setText(R.id.manufacturingCodeView, "出厂编号：${item.manufacturingNo}")
//                    .setText(R.id.sampleCodeView, "样品编号：${item.sampleNo}")
//                    .setText(R.id.validDateView, "有效期至：${item.validDeadline}")
//            }
//        }
//        recyclerView.addItemDecoration(
//            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
//        )
//        (recyclerView.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
//        recyclerView.adapter = normalRecyclerAdapter
//        normalRecyclerAdapter.setOnCheckedListener(object :
//            NormalRecyclerAdapter.OnItemClickedListener<SampleListModel.DataModel.RowsModel> {
//            override fun onItemClicked(position: Int, t: SampleListModel.DataModel.RowsModel) {
//                Log.d(kTag, t.id)
//            }
//        })

        /**
         * 单选
         * */
//        val singleChoiceAdapter = object : SingleChoiceAdapter<SampleListModel.DataModel.RowsModel>(
//            R.layout.item_select_sample_lv, models
//        ) {
//            override fun convertView(
//                viewHolder: ViewHolder, position: Int, item: SampleListModel.DataModel.RowsModel
//            ) {
//                viewHolder.setText(R.id.sampleNameView, "${item.sampleName}【${item.sampleModel}】")
//                    .setText(R.id.manufacturingCodeView, "出厂编号：${item.manufacturingNo}")
//                    .setText(R.id.sampleCodeView, "样品编号：${item.sampleNo}")
//                    .setText(R.id.validDateView, "有效期至：${item.validDeadline}")
//            }
//        }
//        recyclerView.addItemDecoration(
//            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
//        )
//        (recyclerView.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
//        recyclerView.adapter = singleChoiceAdapter
//        singleChoiceAdapter.setOnCheckedListener(object :
//            SingleChoiceAdapter.OnItemCheckedListener<SampleListModel.DataModel.RowsModel> {
//            override fun onItemChecked(position: Int, t: SampleListModel.DataModel.RowsModel) {
//                Log.d(kTag, t.id)
//            }
//        })

        /**
         * 多选
         * */
        val multipleChoiceAdapter =
            object : MultipleChoiceAdapter<SampleListModel.DataModel.RowsModel>(
                R.layout.item_select_sample_lv, models
            ) {
                override fun convertView(
                    viewHolder: ViewHolder, position: Int, item: SampleListModel.DataModel.RowsModel
                ) {
                    viewHolder.setText(
                        R.id.sampleNameView,
                        "${item.sampleName}【${item.sampleModel}】"
                    )
                        .setText(R.id.manufacturingCodeView, "出厂编号：${item.manufacturingNo}")
                        .setText(R.id.sampleCodeView, "样品编号：${item.sampleNo}")
                        .setText(R.id.validDateView, "有效期至：${item.validDeadline}")
                }
            }
        recyclerView.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )
        (recyclerView.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
        recyclerView.adapter = multipleChoiceAdapter
        multipleChoiceAdapter.setOnCheckedListener(object :
            MultipleChoiceAdapter.OnItemCheckedListener<SampleListModel.DataModel.RowsModel> {
            override fun onItemChecked(
                position: Int, items: ArrayList<SampleListModel.DataModel.RowsModel>
            ) {
                Log.d("Casic", "MainActivity => onItemChecked: ${items.toJson()}")
            }
        })
    }
}