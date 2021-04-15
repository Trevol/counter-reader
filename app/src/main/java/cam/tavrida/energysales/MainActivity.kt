package cam.tavrida.energysales

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

class MainActivity : ComponentActivity() {
    val rotation =  mutableStateOf(0 to 0)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rotation.value = getRotation()
        setContent {
            AppScreen()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    fun getRotation(): Pair<Int, Int> {
        val rotationIdToDegrees = mapOf(0 to 0, 3 to 90, 1 to 270)
        val rotationId = display!!.rotation
        return rotationId to rotationIdToDegrees[rotationId]!!
//        (getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        rotation.value = getRotation()
    }

    companion object {
        fun Any.println() = println(this)
    }
}