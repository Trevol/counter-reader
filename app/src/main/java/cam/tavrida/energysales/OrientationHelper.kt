package cam.tavrida.energysales

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

object orientationHelper {
    @Composable
    fun landscape(): Boolean {
        return LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
    }

    @Composable
    fun portrait(): Boolean {
        return LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT
    }
}