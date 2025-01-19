package com.pengxh.kt.lib.fragments.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pengxh.kt.lib.R
import com.pengxh.kt.lib.databinding.FragmentWidgetSlideBarViewBinding
import com.pengxh.kt.lib.model.CityModel
import com.pengxh.kt.lib.utils.LocaleConstant
import com.pengxh.kt.lite.adapter.NormalRecyclerAdapter
import com.pengxh.kt.lite.adapter.ViewHolder
import com.pengxh.kt.lite.base.KotlinBaseFragment
import com.pengxh.kt.lite.divider.RecyclerStickDecoration
import com.pengxh.kt.lite.extensions.dp2px
import com.pengxh.kt.lite.extensions.getHanYuPinyin
import com.pengxh.kt.lite.extensions.show
import com.pengxh.kt.lite.widget.SlideBarView
import java.text.Collator
import java.util.Collections
import java.util.Locale

class SlideBarViewFragment : KotlinBaseFragment<FragmentWidgetSlideBarViewBinding>() {

    private val kTag = "SlideBarActivity"
    private val stickDecoration by lazy { RecyclerStickDecoration() }

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentWidgetSlideBarViewBinding {
        return FragmentWidgetSlideBarViewBinding.inflate(inflater, container, false)
    }

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

    override fun setupTopBarLayout() {

    }

    override fun initOnCreate(savedInstanceState: Bundle?) {
        val cityBeans = sortCity()
        val cityAdapter = object : NormalRecyclerAdapter<CityModel>(
            R.layout.item_slide_bar_rv_l, cityBeans
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
        binding.cityRecyclerView.layoutManager = layoutManager
        stickDecoration.setContext(requireContext()).setTopGap(24.dp2px(requireContext()))
            .setViewGroupListener(
                object : RecyclerStickDecoration.ViewGroupListener {
                    override fun groupTag(position: Int): Long {
                        return cityBeans[position].initial[0].code.toLong()
                    }

                    override fun groupFirstLetter(position: Int): String {
                        return cityBeans[position].initial
                    }
                }).build()
        binding.cityRecyclerView.addItemDecoration(stickDecoration)
        binding.slideBarView.attachToRecyclerView(
            binding.cityRecyclerView, LocaleConstant.CITIES.toMutableList()
        )
        binding.cityRecyclerView.adapter = cityAdapter
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
        binding.slideBarView.setOnLetterIndexChangeListener(object :
            SlideBarView.OnLetterIndexChangeListener {
            override fun onLetterIndexChange(letter: String) {
                //根据滑动显示的字母索引到城市名字第一个汉字
                val letterPosition = binding.slideBarView.getLetterPosition(letter)
                if (letterPosition != -1) {
                    binding.cityRecyclerView.smoothScrollToPosition(letterPosition)
                }
            }
        })
    }
}