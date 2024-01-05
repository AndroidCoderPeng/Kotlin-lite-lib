package com.pengxh.kt.lib.fragments.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import com.pengxh.kt.lib.R
import com.pengxh.kt.lib.databinding.FragmentAdapterNormalRecyclerBinding
import com.pengxh.kt.lite.adapter.NormalRecyclerAdapter
import com.pengxh.kt.lite.adapter.ViewHolder
import com.pengxh.kt.lite.base.KotlinBaseFragment
import com.pengxh.kt.lite.extensions.show
import com.pengxh.kt.lite.extensions.timestampToCompleteDate

class NormalRecyclerAdapterFragment : KotlinBaseFragment<FragmentAdapterNormalRecyclerBinding>() {

    private val kTag = "NormalRecyclerAdapterFragment"
    private val items: MutableList<String> = ArrayList()

    init {
        for (i in 1..20) {
            items.add("普通列表-${System.currentTimeMillis().timestampToCompleteDate()}")
        }
    }

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAdapterNormalRecyclerBinding {
        return FragmentAdapterNormalRecyclerBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarLayout() {

    }

    override fun initOnCreate(savedInstanceState: Bundle?) {
        val normalAdapter = object : NormalRecyclerAdapter<String>(R.layout.item_normal_rv_l, items) {
            override fun convertView(viewHolder: ViewHolder, position: Int, item: String) {
                viewHolder.setText(R.id.textView, item)
            }
        }
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        )
        binding.recyclerView.adapter = normalAdapter
        normalAdapter.setOnItemClickedListener(object :
            NormalRecyclerAdapter.OnItemClickedListener<String> {
            override fun onItemClicked(position: Int, t: String) {
                t.show(requireContext())
            }
        })
    }

    override fun observeRequestState() {

    }

    override fun initEvent() {

    }
}