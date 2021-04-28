package com.tavrida.energysales.ui.components.counter

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tavrida.energysales.data_access.models.Counter
import com.tavrida.utils.toStringOrEmpty

object CounterPropertyGrid {
    private object rowStyle {
        val padding = Modifier.padding(0.dp, 5.dp)
    }

    private object propertyStyle {
        val width = Modifier.width(120.dp)
        val textStyle = TextStyle(fontWeight = FontWeight.W500)
        val fontSize = 16.sp
    }

    private object valueStyle {
        val textStyle = TextStyle()
        val fontSize = 16.sp
    }

    @Composable
    private fun ValueCell(value: Any) {
        Text(
            text = value.toString(),
            fontSize = valueStyle.fontSize
        )
    }

    @JvmName("ValueCellNullable")
    @Composable
    private fun ValueCell(value: Any?) {
        ValueCell(value.toStringOrEmpty())
    }

    @Composable
    private fun PropertyCell(prop: Any) {
        Text(
            text = prop.toString(),
            modifier = propertyStyle.width,
            style = propertyStyle.textStyle,
            fontSize = propertyStyle.fontSize
        )
    }

    @Composable
    private fun PropertyRow(prop: Any, value: Any?) {
        PropertyRow(prop = prop) {
            ValueCell(value)
        }
    }

    @Composable
    private fun PropertyRow(prop: Any, valueContent: @Composable () -> Unit) {
        Row(modifier = rowStyle.padding, verticalAlignment = Alignment.CenterVertically) {
            PropertyCell(prop)
            valueContent()
        }
    }

    @Composable
    operator fun invoke(counter: Counter, modifier: Modifier = Modifier) {
        Column(modifier = modifier) {
            PropertyRow("Заводской №", counter.serialNumber)
            PropertyRow("К трансф", counter.K)
            PropertyRow("Примечание", counter.comment)
            PropertyRow("Расход", counter.prevReading.consumption)
            PropertyRow("Пред. показ.", counter.prevReading.reading)
            PropertyRow("Наст. показ.") {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clickable { }
                ) {
                    ValueCell(value = counter.currentReading?.reading)
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = "Редактировать показания"
                    )
                }
            }
        }
    }
}