package com.tavrida.utils

import android.app.AlertDialog
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Error

fun confirm(context: Context, message: String, no: () -> Unit = {}, yes: () -> Unit) {
    AlertDialog.Builder(context)
        //.setTitle(R.string.app_name)
        .setMessage(message)
        .setPositiveButton("Да") { dialog, id ->
            dialog.dismiss()
            yes()
        }
        .setNegativeButton("Нет") { dialog, id ->
            dialog.dismiss()
            no()
        }
        .create()
        .show()
}

fun error(context: Context, message: String) {
    Icons.Outlined.Error
    AlertDialog.Builder(context)
        //.setTitle(R.string.app_name)
        // .setIcon(Drawable())
        .setMessage(message)
        .setNegativeButton("Закрыть") { dialog, id ->
            dialog.dismiss()
        }
        .create()
        .show()
}