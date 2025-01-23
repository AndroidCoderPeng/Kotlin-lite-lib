package com.pengxh.kt.lib.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.pengxh.kt.lib.R
import com.pengxh.kt.lib.databinding.FragmentUtilsPackageBinding
import com.pengxh.kt.lib.fragments.utils.BroadcastReceiverFragment
import com.pengxh.kt.lib.fragments.utils.FileDownloadManagerFragment
import com.pengxh.kt.lib.fragments.utils.GalleryScaleHelperFragment
import com.pengxh.kt.lib.fragments.utils.HtmlRenderEngineFragment
import com.pengxh.kt.lib.fragments.utils.HttpRequestFragment
import com.pengxh.kt.lib.fragments.utils.LoadingDialogFragment
import com.pengxh.kt.lib.fragments.utils.RetrofitFactoryFragment
import com.pengxh.kt.lib.fragments.utils.SaveKeyValuesFragment
import com.pengxh.kt.lib.fragments.utils.SocketFragment
import com.pengxh.kt.lib.fragments.utils.WaterMarkerEngineFragment
import com.pengxh.kt.lite.base.KotlinBaseFragment

class UtilsPackageFragment : KotlinBaseFragment<FragmentUtilsPackageBinding>() {

    private val itemTitles = arrayOf(
        "Socket",
        "广播接受者管理器",
        "文件下载管理器",
        "模糊背景画廊",
        "HTML富文本渲染",
        "Http请求",
        "加载对话框",
        "Retrofit构造器",
        "SharedPreferences",
        "水印绘制引擎"
    )
    private val fragmentPages = mutableListOf<Fragment>()

    private fun getFragmentAt(position: Int): Fragment {
        if (position < fragmentPages.size) {
            return fragmentPages[position]
        }
        val fragment = when (position) {
            0 -> SocketFragment()
            1 -> BroadcastReceiverFragment()
            2 -> FileDownloadManagerFragment()
            3 -> GalleryScaleHelperFragment()
            4 -> HtmlRenderEngineFragment()
            5 -> HttpRequestFragment()
            6 -> LoadingDialogFragment()
            7 -> RetrofitFactoryFragment()
            8 -> SaveKeyValuesFragment()
            9 -> WaterMarkerEngineFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
        fragmentPages.add(fragment)
        return fragment
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
        binding.spinner.adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, itemTitles)
        binding.spinner.setSelection(0)
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                switchPage(getFragmentAt(position))
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    override fun observeRequestState() {

    }

    override fun initEvent() {

    }

    private fun switchPage(description: Fragment) {
        val transition = childFragmentManager.beginTransaction()
        transition.replace(R.id.contentLayout, description)
        transition.commit()
    }
}