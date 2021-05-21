package com.tavrida.utils

import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import java.lang.IllegalStateException

fun Activity.fixCurrentOrientation(): Orientation =
    when (resources.configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            Orientation.LANDSCAPE
        }
        Configuration.ORIENTATION_PORTRAIT -> {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            Orientation.PORTRAIT
        }
        else -> {
            throw IllegalStateException("Unexpected oriemtation value ${resources.configuration.orientation}")
        }
    }


enum class Orientation { PORTRAIT, LANDSCAPE }
