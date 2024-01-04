package com.pengxh.kt.lib.fragments.extensions

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.ImageFormat
import android.graphics.RectF
import android.media.FaceDetector
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.Surface
import android.view.ViewGroup
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraInfo
import androidx.camera.core.CameraSelector
import androidx.camera.core.CameraState
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.common.util.concurrent.ListenableFuture
import com.pengxh.kt.lib.databinding.FragmentImageExtensionBinding
import com.pengxh.kt.lite.base.KotlinBaseFragment
import com.pengxh.kt.lite.extensions.toBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutionException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import kotlin.math.abs

class ImageExtensionFragment : KotlinBaseFragment<FragmentImageExtensionBinding>() {

    companion object {
        private const val RATIO_4_3_VALUE = 4.0 / 3.0
        private const val RATIO_16_9_VALUE = 16.0 / 9.0
    }

    private val kTag = "FaceCollectionActivity"
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var imageCapture: ImageCapture
    private lateinit var imageAnalysis: ImageAnalysis
    private val executor = ThreadPoolExecutor(
        16, 16,
        0L, TimeUnit.MILLISECONDS,
        LinkedBlockingQueue(1024),
        ThreadPoolExecutor.AbortPolicy()
    )

    //分配人脸空间
    private lateinit var faces: Array<FaceDetector.Face?>
    private val maxFaceCount = 1
    private lateinit var rectF: RectF

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentImageExtensionBinding {
        return FragmentImageExtensionBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarLayout() {

    }

    override fun initOnCreate(savedInstanceState: Bundle?) {
        // Initialize our background executor
        cameraExecutor = Executors.newSingleThreadExecutor()
        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
    }

    private fun bindPreview(cameraProvider: ProcessCameraProvider) {
        val screenAspectRatio = if (Build.VERSION.SDK_INT >= 30) {
            val metrics = requireActivity().windowManager.currentWindowMetrics.bounds
            aspectRatio(metrics.width(), metrics.height())
        } else {
            val outMetrics = DisplayMetrics()
            requireActivity().windowManager.defaultDisplay.getMetrics(outMetrics)
            aspectRatio(outMetrics.widthPixels, outMetrics.heightPixels)
        }

        // CameraSelector
        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
            .build()

        // Preview
        val cameraPreViewBuilder = Preview.Builder()
            .setTargetAspectRatio(screenAspectRatio)
            .setTargetRotation(Surface.ROTATION_0)
            .build()

        // ImageCapture
        imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .setTargetAspectRatio(screenAspectRatio)
            .setTargetRotation(Surface.ROTATION_0)
            .build()

        // ImageAnalysis
        imageAnalysis = ImageAnalysis.Builder()
            .setTargetAspectRatio(screenAspectRatio)
            .setTargetRotation(Surface.ROTATION_0)
            .build()

        // Must unbind the use-cases before rebinding them
        cameraProvider.unbindAll()
        try {
            val camera = cameraProvider.bindToLifecycle(
                this,
                cameraSelector,
                imageCapture,
                imageAnalysis,
                cameraPreViewBuilder
            )

            // Attach the viewfinder's surface provider to preview use case
            cameraPreViewBuilder.setSurfaceProvider(binding.cameraPreView.surfaceProvider)
            observeCameraState(camera.cameraInfo)
        } catch (e: Exception) {
            Log.e(kTag, "Use case binding failed", e)
        }
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun observeCameraState(cameraInfo: CameraInfo) {
        cameraInfo.cameraState.observe(this) { cameraState ->
            //开始预览之后才人脸检测
            if (cameraState.type == CameraState.Type.OPEN) {
                imageAnalysis.setAnalyzer(cameraExecutor) { imageProxy ->
                    /**
                     * CameraX 可通过 setOutputImageFormat(int) 支持 YUV_420_888 和 RGBA_8888。默认格式为 YUV_420_888
                     *
                     * NV12是iOS中有的模式，它的存储顺序是先存Y分量，再YV进行交替存储。
                     * NV21是Android中有的模式，它的存储顺序是先存Y分量，再VU交替存储。
                     * NV12和NV21格式都属于YUV420SP类型
                     */
                    if (imageProxy.format == ImageFormat.YUV_420_888) {
                        executor.execute {
                            val image = imageProxy.image

                            val bitmap = image?.toBitmap(ImageFormat.YUV_420_888) ?: return@execute

                            faces = arrayOfNulls(maxFaceCount)

                            /**
                             * Android内置的人脸识别，需要将Bitmap对象转为RGB_565格式，否则无法识别
                             */
                            val faceBitmap = bitmap.copy(Bitmap.Config.RGB_565, true)
                            val faceDetector = FaceDetector(
                                faceBitmap.width, faceBitmap.height, maxFaceCount
                            )

                            val faceCount = faceDetector.findFaces(faceBitmap, faces)
                            lifecycleScope.launch(Dispatchers.Main) {
                                binding.faceCountView.text = "识别到的人脸数量：${faceCount}"
                            }

                            //检测完之后close就会继续生成下一帧图片，否则就会被阻塞不会继续生成下一帧。
                            imageProxy.close()
                        }
                    }
                }
            }
        }
    }

    private fun aspectRatio(width: Int, height: Int): Int {
        val ratio = width.coerceAtLeast(height).toDouble() / width.coerceAtMost(height)
        return if (abs(ratio - RATIO_4_3_VALUE) <= abs(ratio - RATIO_16_9_VALUE)
        ) {
            AspectRatio.RATIO_4_3
        } else AspectRatio.RATIO_16_9
    }

    override fun observeRequestState() {

    }

    override fun initEvent() {
        binding.openCameraButton.setOnClickListener {
            // 检查 CameraProvider 可用性
            cameraProviderFuture.addListener({
                try {
                    val cameraProvider = cameraProviderFuture.get()
                    bindPreview(cameraProvider)
                } catch (e: ExecutionException) {
                    e.printStackTrace()
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }, ContextCompat.getMainExecutor(requireContext()))
        }
    }
}