package com.pengxh.kt.lib.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pengxh.kt.lib.R
import com.pengxh.kt.lib.adapter.SlideAdapter
import com.pengxh.kt.lib.databinding.FragmentWidgetPackageBinding
import com.pengxh.kt.lib.fragments.widget.AirDashBoardViewFragment
import com.pengxh.kt.lib.fragments.widget.AudioFragment
import com.pengxh.kt.lib.fragments.widget.CircleProgressBarFragment
import com.pengxh.kt.lib.fragments.widget.DeleteEditTextFragment
import com.pengxh.kt.lib.fragments.widget.DialogFragment
import com.pengxh.kt.lib.fragments.widget.EasyPopupWindowFragment
import com.pengxh.kt.lib.fragments.widget.EmptyViewFragment
import com.pengxh.kt.lib.fragments.widget.KeyBoardViewFragment
import com.pengxh.kt.lib.fragments.widget.NoScrollViewPagerFragment
import com.pengxh.kt.lib.fragments.widget.SlideBarViewFragment
import com.pengxh.kt.lib.fragments.widget.SteeringWheelViewFragment
import com.pengxh.kt.lib.fragments.widget.TitleBarViewFragment
import com.pengxh.kt.lite.base.KotlinBaseFragment

class WidgetPackageFragment : KotlinBaseFragment<FragmentWidgetPackageBinding>() {

    private lateinit var slideAdapter: SlideAdapter
    private val itemTitles = arrayOf(
        "音频录制及播放",
        "对话框",
        "空气指数表盘",
        "圆形进度条",
        "带删除的输入框",
        "PopupWindow",
        "空白页面",
        "数字键盘",
        "NoScrollViewPager",
        "联系人侧边栏",
        "方向控制盘",
        "顶部标题栏"
    )
    private var fragmentPages: ArrayList<Fragment> = ArrayList()

    init {
        fragmentPages.add(AudioFragment())
        fragmentPages.add(DialogFragment())
        fragmentPages.add(AirDashBoardViewFragment())
        fragmentPages.add(CircleProgressBarFragment())
        fragmentPages.add(DeleteEditTextFragment())
        fragmentPages.add(EasyPopupWindowFragment())
        fragmentPages.add(EmptyViewFragment())
        fragmentPages.add(KeyBoardViewFragment())
        fragmentPages.add(NoScrollViewPagerFragment())
        fragmentPages.add(SlideBarViewFragment())
        fragmentPages.add(SteeringWheelViewFragment())
        fragmentPages.add(TitleBarViewFragment())
    }

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentWidgetPackageBinding {
        return FragmentWidgetPackageBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarLayout() {

    }

    override fun initOnCreate(savedInstanceState: Bundle?) {
        slideAdapter = SlideAdapter(requireContext(), itemTitles)
        binding.listView.adapter = slideAdapter

        //默认选中第一个
        slideAdapter.setSelectItem(0)
        slideAdapter.notifyDataSetInvalidated()

        //显示首页
        switchPage(fragmentPages[0])
    }

    override fun observeRequestState() {

    }

    override fun initEvent() {
        binding.listView.setOnItemClickListener { _, _, position, _ ->
            slideAdapter.setSelectItem(position)
            slideAdapter.notifyDataSetInvalidated()

            //切换页面
            switchPage(fragmentPages[position])
        }
    }

    private fun switchPage(description: Fragment) {
        val transition = childFragmentManager.beginTransaction()
        transition.replace(R.id.contentLayout, description)
        transition.commit()
    }
}