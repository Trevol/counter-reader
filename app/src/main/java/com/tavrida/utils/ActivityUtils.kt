package com.tavrida.utils

import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.res.Configuration

fun Activity.fixCurrentOrientation() {
    when (resources.configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
        Configuration.ORIENTATION_PORTRAIT -> {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }
}
