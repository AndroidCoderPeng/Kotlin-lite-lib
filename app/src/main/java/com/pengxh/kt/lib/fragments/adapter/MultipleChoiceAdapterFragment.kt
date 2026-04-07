package com.pengxh.kt.lib.fragments.adapter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pengxh.kt.lib.R
import com.pengxh.kt.lib.databinding.FragmentAdapterMultipleChoiceBinding
import com.pengxh.kt.lite.adapter.MultipleChoiceAdapter
import com.pengxh.kt.lite.adapter.ViewHolder
import com.pengxh.kt.lite.base.KotlinBaseFragment
import com.pengxh.kt.lite.extensions.toJson

class MultipleChoiceAdapterFragment : KotlinBaseFragment<FragmentAdapterMultipleChoiceBinding>() {

    private val kTag = "MultipleChoiceAdapterFragment"

    data class Item(
        val id: Int,           // 稳定的唯一标识
        val title: String,     // 显示文本
        val createTime: String // 时间戳（仅显示用）
    )

    private val items: MutableList<Item> = ArrayList()
    private lateinit var selectedAdapter: MultipleChoiceAdapter<Item>
    private var isLoading = false

    init {
        for (i in 1..20) {
            items.add(Item(i, "多选列表", System.currentTimeMillis().toString()))
        }
    }

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAdapterMultipleChoiceBinding {
        return FragmentAdapterMultipleChoiceBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarLayout() {

    }

    override fun initOnCreate(savedInstanceState: Bundle?) {
        selectedAdapter = object : MultipleChoiceAdapter<Item>(
            R.layout.item_multiple_choice_rv_l, items
        ) {
            override fun convertView(viewHolder: ViewHolder, position: Int, item: Item) {
                viewHolder.setText(R.id.textView, "${item.title} ${item.createTime}")
            }
        }
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        )
        binding.recyclerView.adapter = selectedAdapter
        selectedAdapter.setOnItemCheckedListener(object :
            MultipleChoiceAdapter.OnItemCheckedListener<Item> {
            override fun onItemChecked(items: List<Item>) {
                Log.d(kTag, "onItemChecked => ${items.toJson()}")
            }
        })
    }

    override fun observeRequestState() {

    }

    override fun initEvent() {
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisiblePosition = layoutManager.findLastVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount

                // 滑到底部且未在加载中
                if (!isLoading && lastVisiblePosition >= totalItemCount - 1) {
                    loadMore()
                }
            }
        })
    }

    private fun loadMore() {
        isLoading = true
        val startId = items.size + 1
        val newItems = ArrayList<Item>()
        for (i in startId until startId + 20) {
            newItems.add(Item(i, "多选列表", System.currentTimeMillis().toString()))
        }
        selectedAdapter.loadMore(newItems)
        isLoading = false
    }
}