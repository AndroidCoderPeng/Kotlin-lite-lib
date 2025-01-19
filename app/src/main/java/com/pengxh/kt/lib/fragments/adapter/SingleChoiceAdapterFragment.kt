package com.pengxh.kt.lib.fragments.adapter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import com.pengxh.kt.lib.R
import com.pengxh.kt.lib.databinding.FragmentAdapterSingleChoiceBinding
import com.pengxh.kt.lite.adapter.SingleChoiceAdapter
import com.pengxh.kt.lite.adapter.ViewHolder
import com.pengxh.kt.lite.base.KotlinBaseFragment
import com.pengxh.kt.lite.extensions.timestampToCompleteDate

class SingleChoiceAdapterFragment : KotlinBaseFragment<FragmentAdapterSingleChoiceBinding>() {

    private val kTag = "SingleChoiceAdapterFragment"
    private val items: MutableList<String> = ArrayList()

    init {
        for (i in 1..20) {
            items.add("单选列表-${System.currentTimeMillis().timestampToCompleteDate()}")
        }
    }

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAdapterSingleChoiceBinding {
        return FragmentAdapterSingleChoiceBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarLayout() {

    }

    override fun initOnCreate(savedInstanceState: Bundle?) {
        val selectedAdapter = object : SingleChoiceAdapter<String>(
            R.layout.item_single_choice_rv_l, items
        ) {
            override fun convertView(viewHolder: ViewHolder, position: Int, item: String) {
                viewHolder.setText(R.id.textView, item)
            }
        }
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        )
        binding.recyclerView.adapter = selectedAdapter
        selectedAdapter.setOnItemCheckedListener(object :
            SingleChoiceAdapter.OnItemCheckedListener<String> {
            override fun onItemChecked(position: Int, item: String) {
                Log.d(kTag, "onItemChecked => $item")
            }
        })
    }

    override fun observeRequestState() {

    }

    override fun initEvent() {

    }
}