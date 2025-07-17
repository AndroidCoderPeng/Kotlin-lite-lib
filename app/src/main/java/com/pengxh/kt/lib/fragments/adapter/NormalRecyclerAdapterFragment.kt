package com.pengxh.kt.lib.fragments.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import com.pengxh.kt.lib.R
import com.pengxh.kt.lib.databinding.FragmentAdapterNormalRecyclerBinding
import com.pengxh.kt.lite.adapter.NormalRecyclerAdapter
import com.pengxh.kt.lite.adapter.ViewHolder
import com.pengxh.kt.lite.base.KotlinBaseFragment
import com.pengxh.kt.lite.extensions.show
import com.pengxh.kt.lite.extensions.timestampToCompleteDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NormalRecyclerAdapterFragment : KotlinBaseFragment<FragmentAdapterNormalRecyclerBinding>() {

    data class ItemBean(var id: String, var title: String)

    private val kTag = "NormalRecyclerAdapterFragment"
    private lateinit var normalAdapter: NormalRecyclerAdapter<ItemBean>
    private var isRefresh = false
    private var isLoadMore = false

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAdapterNormalRecyclerBinding {
        return FragmentAdapterNormalRecyclerBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarLayout() {

    }

    override fun initOnCreate(savedInstanceState: Bundle?) {
        val items = ArrayList<ItemBean>()
        for (i in 1..15) {
            items.add(
                ItemBean(
                    i.toString(),
                    "普通列表-${i}-${System.currentTimeMillis().timestampToCompleteDate()}"
                )
            )
        }
        normalAdapter = object : NormalRecyclerAdapter<ItemBean>(R.layout.item_normal_rv_l, items) {
            override fun convertView(viewHolder: ViewHolder, position: Int, item: ItemBean) {
                viewHolder.setText(R.id.textView, item.title)
            }
        }
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        )
        binding.recyclerView.adapter = normalAdapter
        normalAdapter.setOnItemClickedListener(object :
            NormalRecyclerAdapter.OnItemClickedListener<ItemBean> {
            override fun onItemClicked(position: Int, item: ItemBean) {
                item.title.show(requireContext())
            }
        })
    }

    override fun observeRequestState() {

    }

    private val itemComparator = object : NormalRecyclerAdapter.ItemComparator<ItemBean> {
        override fun areItemsTheSame(oldItem: ItemBean, newItem: ItemBean): Boolean {
            return oldItem.id == newItem.id && oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: ItemBean, newItem: ItemBean): Boolean {
            return oldItem.title == newItem.title
        }
    }

    override fun initEvent() {
        binding.refreshView.setOnRefreshListener {
            isRefresh = true
            lifecycleScope.launch(Dispatchers.Main) {
                val result = withContext(Dispatchers.IO) {
                    val items = ArrayList<ItemBean>()
                    for (i in 1..15) {
                        items.add(
                            ItemBean(
                                i.toString(),
                                "刷新的数据-${i}-${
                                    System.currentTimeMillis().timestampToCompleteDate()
                                }"
                            )
                        )
                    }
                    return@withContext items
                }
                delay(1500)
                binding.refreshView.finishRefresh()
                isRefresh = false
                normalAdapter.refresh(result, itemComparator)
            }
        }

        binding.refreshView.setOnLoadMoreListener {
            isLoadMore = true
            lifecycleScope.launch(Dispatchers.Main) {
                val result = withContext(Dispatchers.IO) {
                    val items = ArrayList<ItemBean>()
                    for (i in 1..5) {
                        items.add(
                            ItemBean(
                                i.toString(),
                                "加载的数据-${i}-${
                                    System.currentTimeMillis().timestampToCompleteDate()
                                }"
                            )
                        )
                    }
                    return@withContext items
                }
                delay(1500)
                binding.refreshView.finishLoadMore()
                isLoadMore = false
                normalAdapter.loadMore(result)
            }
        }
    }
}