package com.tavrida.energysales.ui.components.common

import android.content.Context
import android.util.Size
import android.view.ViewGroup
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.tavrida.utils.rememberMutableStateOf
import java.util.concurrent.Executor
import java.util.concurrent.Executors

@Composable
fun CameraView(
    cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA,
    imageAnalyzer: ImageAnalysis.Analyzer?
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = LocalContext.current.let {
        remember { ProcessCameraProvider.getInstance(it) }
    }
    val analyzerExecutor = remember {
        Executors.newSingleThreadExecutor()
    }

    val cameraProvider = remember {
        cameraProviderFuture.get()
    }

    var camera by rememberMutableStateOf(null as Camera?)
    var torchActivated by rememberMutableStateOf(false)

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            val previewView = createPreviewView(context)
            val mainExecutor = ContextCompat.getMainExecutor(context)

            cameraProviderFuture.addListener({
                // val cameraProvider = cameraProviderFuture.get()
                camera = bindCameraUseCases(
                    cameraProvider,
                    cameraSelector,
                    lifecycleOwner,
                    analyzerExecutor,
                    previewView,
                    imageAnalyzer
                )
                if (torchActivated) {
                    camera?.cameraControl?.enableTorch(true)
                }
            }, mainExecutor)

            previewView
        }
    )

    DisposableEffect(key1 = Unit) {
        onDispose {
            camera?.cameraControl?.enableTorch(false)
            cameraProvider.unbindAll()
        }
    }
}

private fun bindCameraUseCases(
    cameraProvider: ProcessCameraProvider,
    cameraSelector: CameraSelector,
    lifecycleOwner: LifecycleOwner,
    analyzerExecutor: Executor,
    previewView: PreviewView,
    imageAnalyzer: ImageAnalysis.Analyzer?
): Camera {
    val preview = Preview.Builder().build()
        .also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }

    val useCases = if (imageAnalyzer != null) {
        arrayOf(preview, setupImageAnalysis(imageAnalyzer, analyzerExecutor))
    } else {
        arrayOf(preview)
    }


    cameraProvider.unbindAll()
    return cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, *useCases)
}

fun setupImageAnalysis(analyzer: ImageAnalysis.Analyzer, executor: Executor): ImageAnalysis {
    return ImageAnalysis.Builder()
        .setTargetResolution(Size(480, 640))
        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
        .build()
        .apply {
            setAnalyzer(executor, analyzer)
        }
}

private fun createPreviewView(context: Context): PreviewView {
    return PreviewView(context).apply {
        scaleType = PreviewView.ScaleType.FILL_CENTER
        // scaleType = PreviewView.ScaleType.FIT_CENTER
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        // Preview is incorrectly scaled in Compose on some devices without this
        implementationMode = PreviewView.ImplementationMode.COMPATIBLE
    }
}