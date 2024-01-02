package com.pengxh.kt.lib.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.pengxh.kt.lib.R
import com.pengxh.kt.lib.databinding.FragmentRvItemOffsetsBinding
import com.pengxh.kt.lite.adapter.NormalRecyclerAdapter
import com.pengxh.kt.lite.adapter.ViewHolder
import com.pengxh.kt.lite.base.KotlinBaseFragment
import com.pengxh.kt.lite.divider.RecyclerViewItemOffsets
import com.pengxh.kt.lite.extensions.show

class RecyclerViewItemOffsetsFragment : KotlinBaseFragment<FragmentRvItemOffsetsBinding>() {

    private val cities = mutableListOf(
        "安徽",
        "北京",
        "滨海",
        "重庆",
        "大连",
        "恩施",
        "福建",
        "甘肃",
        "广东",
        "广西",
        "贵州",
        "海南",
        "河北",
        "河南",
        "黑龙江",
        "湖北",
        "湖南",
        "黄石",
        "吉林",
        "江苏",
        "江西",
        "锦州",
        "荆门",
        "九江",
        "辽宁",
        "洛阳",
        "内蒙古",
        "宁波",
        "宁夏",
        "青岛",
        "青海",
        "三亚",
        "山东",
        "山西",
        "陕西",
        "上海",
        "深圳",
        "十堰",
        "四川",
        "天津",
        "西藏",
        "厦门",
        "襄阳",
        "孝感",
        "新疆",
        "新乡",
        "忻州",
        "宜昌",
        "云南",
        "湛江",
        "浙江",
        "珠海"
    )

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
            R.layout.item_offsets_rv_l, cities
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