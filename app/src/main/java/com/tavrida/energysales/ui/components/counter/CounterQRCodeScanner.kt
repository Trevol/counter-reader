package com.tavrida.energysales.ui.components.counter

import aa.example.composeexperiments.camera.QRAnalyzer
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.Rect
import android.util.Size
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.mlkit.vision.barcode.Barcode
import com.tavrida.energysales.ui.components.common.CameraView
import com.tavrida.energysales.ui.components.common.FixedOrientation
import com.tavrida.energysales.ui.components.common.OrientationSelector
import com.tavrida.utils.Orientation
import com.tavrida.utils.rememberMutableStateOf
import com.tavrida.utils.suppressedClickable
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CounterQRCodeScanner(onDismiss: () -> Unit, onCounterSerialNumberReady: (String) -> Unit) {
    var counterBarcodeInfo by remember {
        mutableStateOf(null as SnBarcodeImage?)
    }

    if (counterBarcodeInfo == null) {
        CounterQRScanningStage(
            onDismiss = onDismiss,
            onCounterInfo = {
                counterBarcodeInfo = it
            }
        )
    } else {
        SnBarcodePreviewStage(counterBarcodeInfo!!, onCounterSerialNumberReady)
    }
}

@Composable
private fun CounterQRScanningStage(
    onDismiss: () -> Unit,
    onCounterInfo: (SnBarcodeImage?) -> Unit
) {
    var allCodes by rememberMutableStateOf(listOf<String>())
    val scope = rememberCoroutineScope()
    var allCodesJob by rememberMutableStateOf(null as Job?)
    val context = LocalContext.current

    val imageAnalyzer = remember {
        QRAnalyzer(context) { barcodes, image ->
            allCodes = barcodes.sortedBy { it.boundingBox?.exactCenterY() }
                .mapNotNull { it.rawValue }
            allCodesJob?.cancel()
            allCodesJob = scope.launch { delay(2000); allCodes = listOf() }

            onCounterInfo(findCounterInfo(barcodes, image))
        }
    }

    FixedOrientation { orientation ->
        BackHandler(onBack = onDismiss)
        CameraView(
            imageAnalyzer = imageAnalyzer,
            analysisTargetResolution = targetResolution(orientation)
        )

        FloatingActionButton(onClick = onDismiss, modifier = Modifier.padding(4.dp)) {
            Icon(Icons.Outlined.ArrowBack, "Назад")
        }

        RawCodes(allCodes)
    }
}

private fun targetResolution(orientation: Orientation) = when (orientation) {
    Orientation.PORTRAIT -> Size(600, 800)
    Orientation.LANDSCAPE -> Size(800, 600)
}

@Composable
fun RawCodes(allCodes: List<String>) {
    if (allCodes.isNotEmpty()) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(5.dp)
                    .background(Color.White)
            ) {
                allCodes.forEach {
                    Text(it, modifier = Modifier.padding(2.dp))
                }
            }
        }
    }
}

@Composable
private fun SnBarcodePreviewStage(
    info: SnBarcodeImage,
    onCounterSerialNumberReady: (String) -> Unit,
    delay: Long = 2000
) {
    Image(
        bitmap = info.barcode.draw(info.image).asImageBitmap(),
        contentDescription = null,
        modifier = Modifier
            .fillMaxSize()
            .suppressedClickable()
            .background(Color.Gray)
    )
    LaunchedEffect(key1 = info.serialNumber) {
        delay(delay)
        onCounterSerialNumberReady(info.serialNumber)
    }
}

private data class SnBarcodeImage(val serialNumber: String, val barcode: Barcode, val image: Bitmap)

private fun findCounterInfo(barcodes: List<Barcode>, image: Bitmap): SnBarcodeImage? {
    val allCodes = sequence {
        barcodes.forEach {
            val sn = it.tryParseCounterSN()
            if (sn != null)
                yield(SnBarcodeImage(sn, it, image))
        }
    }
    return allCodes.minByOrNull { it.barcode.distToCenter(image) }
}


private fun Barcode.distToCenter(image: Bitmap): Float {
    return this.boundingBox!!.exactCenter().squaredDistTo(image.center())
}

private fun Rect.exactCenter() = PointF(exactCenterX(), exactCenterY())
private fun Bitmap.center() = PointF(width / 2f, height / 2f)
private fun PointF.squaredDistTo(other: PointF) = (other.x - x).squared() + (other.y - y).squared()
private inline fun Float.squared() = this * this
val BARCODE_PAINT = Paint()
    .apply {
        style = Paint.Style.STROKE
        strokeWidth = 2f
        color = android.graphics.Color.GREEN
    }

private fun Barcode.draw(image: Bitmap): Bitmap {
    val canvas = android.graphics.Canvas(image)
    val boundingBox = boundingBox!!
    canvas.drawRect(boundingBox, BARCODE_PAINT)
    return image
}

private fun Barcode.tryParseCounterSN(): String? {
    val value = rawValue ?: return null
    if (value.startsWith(COUNTER_CODE_PREFIX)) {
        val sn = value.substring(COUNTER_CODE_PREFIX.length)
        sn.toIntOrNull() ?: return null
        return sn
    } else {
        return null
    }
}

private const val COUNTER_CODE_PREFIX = "1:"