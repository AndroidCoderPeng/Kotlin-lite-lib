package com.pengxh.kt.lib.fragments.divider

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.pengxh.kt.lib.R
import com.pengxh.kt.lib.databinding.FragmentRvItemBorderBinding
import com.pengxh.kt.lib.utils.LocaleConstant
import com.pengxh.kt.lite.adapter.NormalRecyclerAdapter
import com.pengxh.kt.lite.adapter.ViewHolder
import com.pengxh.kt.lite.base.KotlinBaseFragment
import com.pengxh.kt.lite.divider.RecyclerViewItemBorder
import com.pengxh.kt.lite.extensions.show

class RecyclerViewItemBorderFragment : KotlinBaseFragment<FragmentRvItemBorderBinding>() {

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentRvItemBorderBinding {
        return FragmentRvItemBorderBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarLayout() {

    }

    override fun initOnCreate(savedInstanceState: Bundle?) {
        val cityAdapter = object : NormalRecyclerAdapter<String>(
            R.layout.item_offsets_rv_l, LocaleConstant.cities
        ) {
            override fun convertView(viewHolder: ViewHolder, position: Int, item: String) {
                viewHolder.setText(R.id.cityName, item)
            }
        }
        binding.recyclerView.addItemDecoration(RecyclerViewItemBorder(20, 10, 20, 10))
        binding.recyclerView.adapter = cityAdapter
        cityAdapter.setOnItemClickedListener(object :
            NormalRecyclerAdapter.OnItemClickedListener<String> {
            override fun onItemClicked(position: Int, item: String) {
                item.show(requireContext())
            }
        })
    }

    override fun observeRequestState() {

    }

    override fun initEvent() {

    }
}