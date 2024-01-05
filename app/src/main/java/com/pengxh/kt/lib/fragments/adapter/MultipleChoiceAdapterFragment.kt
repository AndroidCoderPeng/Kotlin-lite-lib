package com.pengxh.kt.lib.fragments.adapter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import com.pengxh.kt.lib.R
import com.pengxh.kt.lib.databinding.FragmentAdapterMultipleChoiceBinding
import com.pengxh.kt.lite.adapter.MultipleChoiceAdapter
import com.pengxh.kt.lite.adapter.ViewHolder
import com.pengxh.kt.lite.base.KotlinBaseFragment
import com.pengxh.kt.lite.extensions.timestampToCompleteDate
import com.pengxh.kt.lite.extensions.toJson

class MultipleChoiceAdapterFragment : KotlinBaseFragment<FragmentAdapterMultipleChoiceBinding>() {

    private val kTag = "MultipleChoiceAdapterFragment"
    private val items: MutableList<String> = ArrayList()

    init {
        for (i in 1..20) {
            items.add("多选列表-${System.currentTimeMillis().timestampToCompleteDate()}")
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
        val selectedAdapter = object : MultipleChoiceAdapter<String>(
            R.layout.item_multiple_choice_rv_l, items
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
            MultipleChoiceAdapter.OnItemCheckedListener<String> {
            override fun onItemChecked(position: Int, items: ArrayList<String>) {
                Log.d(kTag, "onItemChecked => ${items.toJson()}")
            }
        })
    }

    override fun observeRequestState() {

    }

    override fun initEvent() {

    }
}