package com.pengxh.kt.lib.fragments.divider

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pengxh.kt.lib.R
import com.pengxh.kt.lib.databinding.FragmentRvStickDecorationBinding
import com.pengxh.kt.lib.model.CityModel
import com.pengxh.kt.lite.adapter.NormalRecyclerAdapter
import com.pengxh.kt.lite.adapter.ViewHolder
import com.pengxh.kt.lite.base.KotlinBaseFragment
import com.pengxh.kt.lite.divider.RecyclerStickDecoration
import com.pengxh.kt.lite.extensions.dp2px
import com.pengxh.kt.lite.extensions.getHanYuPinyin
import com.pengxh.kt.lite.extensions.show
import java.text.Collator
import java.util.Collections
import java.util.Locale

class RecyclerStickDecorationFragment : KotlinBaseFragment<FragmentRvStickDecorationBinding>() {

    private val stickDecoration by lazy { RecyclerStickDecoration() }
    private val cities = listOf(
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

    /**
     * 将城市整理成分组数据
     */
    private fun sortCity(): MutableList<CityModel> {
        //先将数据按照字母排序
        Collections.sort(cities, Collator.getInstance(Locale.CHINA))
        //格式化数据
        val cityBeans = ArrayList<CityModel>()
        for (city in cities) {
            val cityBean = CityModel()
            cityBean.city = city
            val firstLetter = city.getHanYuPinyin().substring(0, 1)
            cityBean.initial = firstLetter
            cityBeans.add(cityBean)
        }
        return cityBeans
    }

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentRvStickDecorationBinding {
        return FragmentRvStickDecorationBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarLayout() {

    }

    override fun initOnCreate(savedInstanceState: Bundle?) {
        val cityBeans = sortCity()
        val cityAdapter = object : NormalRecyclerAdapter<CityModel>(
            R.layout.item_stick_decoration_rv_l, cityBeans
        ) {
            override fun convertView(viewHolder: ViewHolder, position: Int, item: CityModel) {
                viewHolder.setText(R.id.cityName, item.city)
            }
        }
        val layoutManager = object : LinearLayoutManager(requireContext()) {
            override fun smoothScrollToPosition(
                recyclerView: RecyclerView, state: RecyclerView.State, position: Int
            ) {
                val scroller = stickDecoration.SmoothGroupTopScroller(recyclerView.context)
                scroller.targetPosition = position
                startSmoothScroll(scroller)
            }
        }
        binding.recyclerView.layoutManager = layoutManager

        /***吸顶***start************************************/
        stickDecoration.setContext(requireContext())
            .setTopGap(24f.dp2px(requireContext()))
            .setViewGroupListener(
                object : RecyclerStickDecoration.ViewGroupListener {
                    override fun groupTag(position: Int): Long {
                        return cityBeans[position].initial[0].code.toLong()
                    }

                    override fun groupFirstLetter(position: Int): String {
                        return cityBeans[position].initial
                    }
                }).build()
        binding.recyclerView.addItemDecoration(stickDecoration)
        /***吸顶***end**********************************/

        binding.recyclerView.adapter = cityAdapter
        cityAdapter.setOnItemClickedListener(object :
            NormalRecyclerAdapter.OnItemClickedListener<CityModel> {
            override fun onItemClicked(position: Int, t: CityModel) {
                t.city.show(requireContext())
            }
        })
    }

    override fun observeRequestState() {

    }

    override fun initEvent() {

    }
}