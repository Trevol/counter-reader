package cam.tavrida.energysales

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun OrientationSelector(content: @Composable () -> Unit) {
    OrientationSelector(portrait = content, landscape = content)
}

@Composable
fun OrientationSelector(
    portrait: @Composable () -> Unit,
    landscape: @Composable () -> Unit,
    unexpected: @Composable () -> Unit = {
        Text("Unexpected orientation")
    }
) {
    when {
        orientationHelper.portrait() -> {
            portrait()
        }
        orientationHelper.landscape() -> {
            landscape()
        }
        else -> {
            unexpected()
        }
    }
}