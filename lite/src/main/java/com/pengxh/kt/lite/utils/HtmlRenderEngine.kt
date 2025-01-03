package com.pengxh.kt.lite.utils

import android.content.Context
import android.os.Build
import android.text.Editable
import android.text.Html
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ImageSpan
import android.view.View
import android.widget.TextView
import com.bumptech.glide.Glide
import com.pengxh.kt.lite.R
import com.pengxh.kt.lite.extensions.convertDrawable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.xml.sax.XMLReader
import java.util.Locale

class HtmlRenderEngine(builder: Builder) {

    class Builder {
        lateinit var context: Context
        lateinit var html: String
        lateinit var textView: TextView
        lateinit var imageSourceListener: OnGetImageSourceListener

        /**
         * 设置上下文
         * */
        fun setContext(context: Context): Builder {
            this.context = context
            return this
        }

        /**
         * 设置html格式的文本
         * */
        fun setHtmlContent(html: String): Builder {
            this.html = html
            return this
        }

        /**
         * 设置显示html格式文本的View
         * */
        fun setTargetView(textView: TextView): Builder {
            this.textView = textView
            return this
        }

        /**
         * 设置html里面图片地址回调监听
         * */
        fun setOnGetImageSourceListener(imageSourceListener: OnGetImageSourceListener): Builder {
            this.imageSourceListener = imageSourceListener
            return this
        }

        fun build(): HtmlRenderEngine {
            if (!::context.isInitialized || !::html.isInitialized || !::textView.isInitialized || !::imageSourceListener.isInitialized) {
                throw IllegalStateException("All properties must be initialized before building.")
            }
            return HtmlRenderEngine(this)
        }
    }

    private val context = builder.context
    private val html = builder.html
    private val textView = builder.textView
    private val listener = builder.imageSourceListener

    fun load() {
        val job = SupervisorJob()
        val scope = CoroutineScope(Dispatchers.Main + job)

        scope.launch(Dispatchers.Main) {
            textView.movementMethod = LinkMovementMethod.getInstance()
            //默认不处理图片先这样简单设置
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                textView.text = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
            } else {
                textView.text = Html.fromHtml(html)
            }
            withContext(Dispatchers.IO) {
                val imageGetter = Html.ImageGetter { source ->
                    val drawable = try {
                        Glide.with(context).load(source).submit().get()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        R.mipmap.load_image_error.convertDrawable(context)!!
                    }

                    var width = drawable.intrinsicWidth
                    var height = drawable.intrinsicHeight

                    //对图片按比例缩放尺寸
                    val scale = textView.width / width.toFloat()
                    width = (scale * width).toInt()
                    height = (scale * height).toInt()
                    drawable.setBounds(0, 0, width, height)
                    //return
                    drawable
                }

                val htmlText = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Html.fromHtml(
                        html, Html.FROM_HTML_MODE_LEGACY, imageGetter, object : Html.TagHandler {
                            override fun handleTag(
                                opening: Boolean, tag: String,
                                output: Editable, xmlReader: XMLReader
                            ) {
                                if (tag.lowercase(Locale.getDefault()) == "img") {
                                    val len = output.length
                                    val images = output.getSpans(
                                        len - 1, len, ImageSpan::class.java
                                    )
                                    val imgSource = images[0].source ?: return
                                    output.setSpan(object : ClickableSpan() {
                                        override fun onClick(widget: View) {
                                            listener.imageSource(imgSource)
                                        }
                                    }, len - 1, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                                }
                            }
                        })
                } else {
                    Html.fromHtml(html, imageGetter, object : Html.TagHandler {
                        override fun handleTag(
                            opening: Boolean, tag: String, output: Editable, xmlReader: XMLReader
                        ) {
                            if (tag.lowercase(Locale.getDefault()) == "img") {
                                val len = output.length
                                val images = output.getSpans(len - 1, len, ImageSpan::class.java)
                                val imgSource = images[0].source ?: return
                                output.setSpan(object : ClickableSpan() {
                                    override fun onClick(widget: View) {
                                        listener.imageSource(imgSource)
                                    }
                                }, len - 1, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                            }
                        }
                    })
                }
                withContext(Dispatchers.Main) {
                    textView.text = htmlText
                }
            }
            job.cancel()
        }
    }

    interface OnGetImageSourceListener {
        fun imageSource(url: String)
    }
}