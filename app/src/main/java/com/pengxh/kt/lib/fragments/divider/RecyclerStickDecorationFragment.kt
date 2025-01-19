package com.pengxh.kt.lib.fragments.divider

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pengxh.kt.lib.R
import com.pengxh.kt.lib.databinding.FragmentRvStickDecorationBinding
import com.pengxh.kt.lib.model.CityModel
import com.pengxh.kt.lib.utils.LocaleConstant
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

    /**
     * 将城市整理成分组数据
     */
    private fun sortCity(): MutableList<CityModel> {
        //先将数据按照字母排序
        Collections.sort(LocaleConstant.CITIES, Collator.getInstance(Locale.CHINA))
        //格式化数据
        val cityBeans = ArrayList<CityModel>()
        for (city in LocaleConstant.CITIES) {
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
            .setTopGap(24.dp2px(requireContext()))
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
            override fun onItemClicked(position: Int, item: CityModel) {
                item.city.show(requireContext())
            }
        })
    }

    override fun observeRequestState() {

    }

    override fun initEvent() {

    }
}