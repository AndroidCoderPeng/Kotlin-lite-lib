package com.pengxh.kt.lib.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pengxh.kt.lib.R
import com.pengxh.kt.lib.adapter.SlideAdapter
import com.pengxh.kt.lib.databinding.FragmentUtilsPackageBinding
import com.pengxh.kt.lib.fragments.utils.BleFragment
import com.pengxh.kt.lib.fragments.utils.BroadcastReceiverFragment
import com.pengxh.kt.lib.fragments.utils.FileDownloadManagerFragment
import com.pengxh.kt.lib.fragments.utils.SocketFragment
import com.pengxh.kt.lite.base.KotlinBaseFragment

class UtilsPackageFragment : KotlinBaseFragment<FragmentUtilsPackageBinding>() {

    private lateinit var slideAdapter: SlideAdapter
    private val itemTitles = arrayOf(
        "低功耗蓝牙",
        "Socket",
        "广播接受者管理器",
        "文件下载管理器",
        "画廊",
        "HTML富文本渲染",
        "Http请求",
        "沉浸式状态栏",
        "加载对话框",
        "Activity栈管理",
        "Retrofit构造器",
        "SharedPreferences",
        "水印绘制引擎",
        "防内存泄漏Handler"
    )
    private var fragmentPages: ArrayList<Fragment> = ArrayList()

    init {
        fragmentPages.add(BleFragment())
        fragmentPages.add(SocketFragment())
        fragmentPages.add(BroadcastReceiverFragment())
        fragmentPages.add(FileDownloadManagerFragment())
        fragmentPages.add(FileDownloadManagerFragment())
        fragmentPages.add(FileDownloadManagerFragment())
        fragmentPages.add(FileDownloadManagerFragment())
        fragmentPages.add(FileDownloadManagerFragment())
        fragmentPages.add(FileDownloadManagerFragment())
        fragmentPages.add(FileDownloadManagerFragment())
        fragmentPages.add(FileDownloadManagerFragment())
        fragmentPages.add(FileDownloadManagerFragment())
        fragmentPages.add(FileDownloadManagerFragment())
        fragmentPages.add(FileDownloadManagerFragment())
    }

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentUtilsPackageBinding {
        return FragmentUtilsPackageBinding.inflate(inflater, container, false)
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