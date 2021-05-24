package com.tavrida.utils

import android.app.AlertDialog
import android.content.Context

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