package aa.example.composeexperiments.camera

import android.content.Context
import android.graphics.Bitmap
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import com.tavrida.utils.CameraImageConverter
import com.tavrida.utils.println
import com.tavrida.utils.printlnStamped

class QRAnalyzer(
    context: Context,
    val scanEveryNthFrame: Int = 3,
    val onBarcodesFound: (List<Barcode>, Bitmap) -> Unit
) : ImageAnalysis.Analyzer {

    private val converter = CameraImageConverter(context)
    private var frameCounter = 0

    override fun analyze(image: ImageProxy) {
        frameCounter++
        if (frameCounter % scanEveryNthFrame != 0) {
            image.close()
            return
        }
        frameCounter = 0
        BarcodeScanner.process(image)
            .addOnSuccessListener { barcodes ->
                if (barcodes.isNotEmpty()) {
                    onBarcodesFound(barcodes, converter.convert(image))
                }
            }
            .addOnFailureListener {
            }
            .addOnCompleteListener {
                image.close()
            }
    }

    private object BarcodeScanner {
        private val instance = BarcodeScanning.getClient(
            BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .build()
        )

        fun process(image: ImageProxy): Task<MutableList<Barcode>> =
            InputImage.fromMediaImage(image.image!!, image.imageInfo.rotationDegrees)
                .let {
                    instance.process(it)
                }
    }
}
