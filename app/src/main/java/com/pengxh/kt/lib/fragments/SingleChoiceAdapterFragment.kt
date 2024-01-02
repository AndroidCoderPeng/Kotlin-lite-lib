package com.pengxh.kt.lib.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import com.pengxh.kt.lib.R
import com.pengxh.kt.lib.databinding.FragmentSingleChoiceAdapterBinding
import com.pengxh.kt.lite.adapter.SingleChoiceAdapter
import com.pengxh.kt.lite.adapter.ViewHolder
import com.pengxh.kt.lite.base.KotlinBaseFragment
import com.pengxh.kt.lite.extensions.timestampToCompleteDate

class SingleChoiceAdapterFragment : KotlinBaseFragment<FragmentSingleChoiceAdapterBinding>() {

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
    ): FragmentSingleChoiceAdapterBinding {
        return FragmentSingleChoiceAdapterBinding.inflate(inflater, container, false)
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
            override fun onItemChecked(position: Int, t: String) {
                Log.d(kTag, "onItemChecked => $t")
            }
        })
    }

    override fun observeRequestState() {

    }

    override fun initEvent() {

    }
}