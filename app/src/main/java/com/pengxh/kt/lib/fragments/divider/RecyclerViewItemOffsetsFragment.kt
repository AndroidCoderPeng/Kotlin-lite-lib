package com.pengxh.kt.lib.fragments.divider

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.pengxh.kt.lib.R
import com.pengxh.kt.lib.databinding.FragmentRvItemOffsetsBinding
import com.pengxh.kt.lib.utils.LocaleConstant
import com.pengxh.kt.lite.adapter.NormalRecyclerAdapter
import com.pengxh.kt.lite.adapter.ViewHolder
import com.pengxh.kt.lite.base.KotlinBaseFragment
import com.pengxh.kt.lite.divider.RecyclerViewItemOffsets
import com.pengxh.kt.lite.extensions.show

class RecyclerViewItemOffsetsFragment : KotlinBaseFragment<FragmentRvItemOffsetsBinding>() {

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentRvItemOffsetsBinding {
        return FragmentRvItemOffsetsBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarLayout() {

    }

    override fun initOnCreate(savedInstanceState: Bundle?) {
        val cityAdapter = object : NormalRecyclerAdapter<String>(
            R.layout.item_offsets_rv_l, LocaleConstant.CITIES.toMutableList()
        ) {
            override fun convertView(viewHolder: ViewHolder, position: Int, item: String) {
                viewHolder.setText(R.id.cityName, item)
            }
        }
        binding.recyclerView.addItemDecoration(RecyclerViewItemOffsets(30, 10, 30, 10))
        binding.recyclerView.adapter = cityAdapter
        cityAdapter.setOnItemClickedListener(object :
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