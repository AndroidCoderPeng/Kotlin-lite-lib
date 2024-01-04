package com.pengxh.kt.lib.view

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.luck.picture.lib.photoview.PhotoView
import com.pengxh.kt.lib.R
import com.pengxh.kt.lib.databinding.ActivityBigImageBinding
import com.pengxh.kt.lite.base.KotlinBaseActivity
import com.pengxh.kt.lite.utils.Constant
import com.pengxh.kt.lite.utils.ImmerseStatusBarUtil

class BigImageActivity : KotlinBaseActivity<ActivityBigImageBinding>() {

    override fun initViewBinding(): ActivityBigImageBinding {
        return ActivityBigImageBinding.inflate(layoutInflater)
    }

    override fun setupTopBarLayout() {
        ImmerseStatusBarUtil.setColor(this, Color.BLACK)
        binding.leftBackView.setOnClickListener { finish() }
    }

    override fun initOnCreate(savedInstanceState: Bundle?) {

    }

    override fun observeRequestState() {

    }

    override fun initEvent() {
        val index = intent.getIntExtra(Constant.BIG_IMAGE_INTENT_INDEX_KEY, 0)
        val urls = intent.getStringArrayListExtra(Constant.BIG_IMAGE_INTENT_DATA_KEY)
        if (urls == null || urls.size == 0) {
            return
        }
        val imageSize = urls.size
        binding.pageIndexView.text = String.format("(" + (index + 1) + "/" + imageSize + ")")
        binding.imagePagerView.adapter = BigImageAdapter(this, urls)
        binding.imagePagerView.currentItem = index
        binding.imagePagerView.offscreenPageLimit = imageSize
        binding.imagePagerView.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int, positionOffset: Float, positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                binding.pageIndexView.text =
                    String.format("(" + (position + 1) + "/" + imageSize + ")")
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    inner class BigImageAdapter(
        private val context: Context, private val data: ArrayList<String>
    ) : PagerAdapter() {

        override fun getCount(): Int = data.size

        override fun isViewFromObject(view: View, any: Any): Boolean {
            return view == any
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val view =
                LayoutInflater.from(context).inflate(R.layout.item_big_picture, container, false)
            val photoView: PhotoView = view.findViewById(R.id.photoView)
            Glide.with(context).load(data[position]).into(photoView)
            photoView.scaleType = ImageView.ScaleType.CENTER_INSIDE
            container.addView(view)
            //点击大图取消预览
            photoView.setOnClickListener { finish() }
            return view
        }

        override fun destroyItem(container: ViewGroup, position: Int, any: Any) {
            container.removeView(any as View)
        }
    }
}