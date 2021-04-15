package cam.tavrida.energysales

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.sp
import cam.tavrida.energysales.ui.theme.CounterReaderTheme

@Composable
fun AppScreen() {
    val content = @Composable {
        var reading by remember {
            mutableStateOf(null as Float?)
        }
        var counterId by remember {
            mutableStateOf(null as Int?)
        }

        OutlinedTextField(
            value = counterId.toStringOrEmpty(),
            onValueChange = { counterId = it.filter { it.isDigit() }.toInt() },

            label = { Text("Счетчик") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        OutlinedTextField(
            value = reading.toStringOrEmpty(),
            onValueChange = { reading = it.toFloat() },
            label = { Text("Показания") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = TextStyle(
                fontSize = 36.sp,
            )
        )
    }

    CounterReaderTheme {
        OrientationSelector(
            portrait = { Column { content() } },
            landscape = { Row { content() } }
        )
    }
}

fun Any?.toStringOrEmpty() = this?.toString() ?: ""

@Preview(showBackground = true)
@Composable
fun AppScreenPreview() {
    AppScreen()
}