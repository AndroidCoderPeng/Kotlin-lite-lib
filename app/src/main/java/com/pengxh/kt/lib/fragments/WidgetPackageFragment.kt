package com.pengxh.kt.lib.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pengxh.kt.lib.R
import com.pengxh.kt.lib.adapter.SlideAdapter
import com.pengxh.kt.lib.databinding.FragmentWidgetPackageBinding
import com.pengxh.kt.lib.fragments.widget.AudioFragment
import com.pengxh.kt.lite.base.KotlinBaseFragment

class WidgetPackageFragment : KotlinBaseFragment<FragmentWidgetPackageBinding>() {

    private lateinit var slideAdapter: SlideAdapter
    private val itemTitles = arrayOf(
        "音频录制及播放",
        "仿iOS对话框",
        "空气指数表盘",
        "圆形进度条",
        "带删除的输入框",
        "PopupWindow",
        "输入法键盘",
        "NoScrollViewPager",
        "联系人侧边栏",
        "方向控制盘"
    )
    private var fragmentPages: ArrayList<Fragment> = ArrayList()

    init {
        fragmentPages.add(AudioFragment())
//        fragmentPages.add(SocketFragment())
//        fragmentPages.add(ActivityStackManagerFragment())
//        fragmentPages.add(BroadcastReceiverFragment())
//        fragmentPages.add(FileDownloadManagerFragment())
//        fragmentPages.add(GalleryScaleHelperFragment())
//        fragmentPages.add(HtmlRenderEngineFragment())
//        fragmentPages.add(HttpRequestFragment())
//        fragmentPages.add(LoadingDialogFragment())
//        fragmentPages.add(RetrofitFactoryFragment())
//        fragmentPages.add(SaveKeyValuesFragment())
//        fragmentPages.add(WaterMarkerEngineFragment())
//        fragmentPages.add(WeakReferenceHandlerFragment())
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