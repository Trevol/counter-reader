package com.tavrida.energysales

import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class AppSettings(val pref: SharedPreferences) {
    val storageDirectory = "tavrida-energy-sales"

    val backendUrl get() = pref.getString(PrefKeys.BACKEND_URL, null)
    val user get() = pref.getString(PrefKeys.USER, null)

    fun editable() = EditableAppSettings(this)
}

class EditableAppSettings(val settings: AppSettings) {
    var backendUrl by mutableStateOf(settings.backendUrl)
    var user by mutableStateOf(settings.user)

    fun apply() {
        with(settings.pref.edit()) {
            putString(PrefKeys.BACKEND_URL, backendUrl)
            putString(PrefKeys.USER, user)
            apply()
        }
    }
}

private object PrefKeys {
    const val USER = "USER"
    const val BACKEND_URL = "BACKEND_URL"
}
