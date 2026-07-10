package com.pengxh.kt.lib.fragments.extensions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import com.pengxh.kt.lib.R
import com.pengxh.kt.lib.databinding.FragmentExtensionLongBinding
import com.pengxh.kt.lite.adapter.NormalRecyclerAdapter
import com.pengxh.kt.lite.adapter.ViewHolder
import com.pengxh.kt.lite.base.KotlinBaseFragment
import com.pengxh.kt.lite.extensions.formatFileSize
import com.pengxh.kt.lite.extensions.millsToTime
import com.pengxh.kt.lite.extensions.timestampToCompleteDate
import com.pengxh.kt.lite.extensions.timestampToDate
import com.pengxh.kt.lite.extensions.timestampToTime

class LongExtensionFragment : KotlinBaseFragment<FragmentExtensionLongBinding>() {

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentExtensionLongBinding {
        return FragmentExtensionLongBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarLayout() {

    }

    override fun initOnCreate(savedInstanceState: Bundle?) {
        val dataModels = ArrayList<LongDataModel>()
        val timeMillis = System.currentTimeMillis()
        dataModels.add(
            LongDataModel("当前时间戳转年月日时分秒", timeMillis.timestampToCompleteDate())
        )
        dataModels.add(
            LongDataModel("时间戳转年月日", timeMillis.timestampToDate())
        )
        dataModels.add(
            LongDataModel("时间戳转时分秒", timeMillis.timestampToTime())
        )
        dataModels.add(
            LongDataModel("当前时间戳转分秒", timeMillis.millsToTime())
        )
        dataModels.add(
            LongDataModel("文件/文件夹大小", timeMillis.formatFileSize())
        )

        val normalAdapter = object : NormalRecyclerAdapter<LongDataModel>(
            R.layout.item_long_extension_rv_l, dataModels
        ) {
            override fun convertView(
                viewHolder: ViewHolder, position: Int, item: LongDataModel
            ) {
                viewHolder.setText(R.id.keyView, item.key)
                    .setText(R.id.valueView, item.value)
            }
        }
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        )
        binding.recyclerView.adapter = normalAdapter
    }

    override fun observeRequestState() {

    }

    override fun initEvent() {

    }

    data class LongDataModel(val key: String, val value: String)
}