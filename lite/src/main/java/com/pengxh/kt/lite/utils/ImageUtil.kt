package com.pengxh.kt.lite.utils

import android.app.Activity
import android.text.Editable
import android.text.Html
import android.text.Html.ImageGetter
import android.text.Html.TagHandler
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ImageSpan
import android.view.View
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.pengxh.kt.lite.activity.BigImageActivity
import com.pengxh.kt.lite.extensions.dp2px
import com.pengxh.kt.lite.extensions.navigatePageTo
import org.xml.sax.XMLReader
import java.util.*
import java.util.concurrent.ExecutionException

object ImageUtil {
    /**
     * 将html字符串中的图片加载出来 设置点击事件 然后TextView进行显示
     *
     * @param activity
     * @param textView
     * @param sources  需要显示的带有html标签的文字
     * @param width    设备屏幕像素宽度
     */
    fun setTextFromHtml(
        activity: Activity?, textView: TextView?, sources: String?, width: Float, rightPadding: Int
    ) {
        if (activity == null || textView == null || sources.toString().isBlank()) {
            return
        }
        synchronized(ImageUtil::class.java) {
            textView.movementMethod = LinkMovementMethod.getInstance()
            textView.text = Html.fromHtml(sources) //默认不处理图片先这样简单设置
            Thread {
                val imageGetter = ImageGetter { source ->
                    try {
                        val drawable = Glide.with(activity).asDrawable().load(source)
                            .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get()
                            ?: return@ImageGetter null
                        var w = drawable.intrinsicWidth
                        var h = drawable.intrinsicHeight
                        //对图片改变尺寸
                        val scale = width / w
                        w = (scale * w - rightPadding.toFloat().dp2px(activity)).toInt()
                        h = (scale * h).toInt()
                        drawable.setBounds(0, 0, w, h)
                        return@ImageGetter drawable
                    } catch (e: ExecutionException) {
                        e.printStackTrace()
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                    null
                }
                val charSequence: CharSequence =
                    Html.fromHtml(sources, imageGetter, ImageClickHandler(activity))
                activity.runOnUiThread(Runnable { textView.text = charSequence })
            }.start()
        }
    }

    private class ImageClickHandler(private val mActivity: Activity) :
        TagHandler {
        override fun handleTag(
            opening: Boolean, tag: String, output: Editable, xmlReader: XMLReader
        ) {
            //获取传入html文本里面包含的所有Tag，然后取出img开头的
            if (tag.lowercase(Locale.getDefault()) == "img") {
                val len = output.length
                // 获取图片地址
                val images = output.getSpans(len - 1, len, ImageSpan::class.java)
                val imgURL = images[0].source!!
                // 使图片可点击并监听点击事件
                output.setSpan(
                    ClickableImage(mActivity, imgURL),
                    len - 1,
                    len,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }

        private class ClickableImage(
            private val mActivity: Activity, private val imageURL: String
        ) : ClickableSpan() {
            override fun onClick(widget: View) {
                //查看大图
                val urls = ArrayList<String>()
                urls.add(imageURL)
                mActivity.navigatePageTo<BigImageActivity>(0, urls)
            }
        }
    }
}