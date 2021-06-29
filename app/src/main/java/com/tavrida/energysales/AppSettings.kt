package com.tavrida.energysales

import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/*object AppSettingsO {
    const val user = "Саша"

    // const val backendUrl = "http://192.168.0.112:8080"
    const val backendUrl = "http://195.230.112.210:8080" //TODO: не коммитить!!!
    val STORAGE_DIR = "tavrida-energy-sales"
}*/

class AppSettings(val pref: SharedPreferences) {
    val storageDirectory = "tavrida-energy-sales"
    val user = "Саша"

    val backendUrl get() = pref.getString(PrefKeys.BACKEND_URL, null)

    fun editable() = EditableAppSettings(this)
}

class EditableAppSettings(val settings: AppSettings) {
    var backendUrl by mutableStateOf(settings.backendUrl)
    fun apply() {
        with(settings.pref.edit()) {
            putString(PrefKeys.BACKEND_URL, backendUrl)
            apply()
        }
    }
}

private object PrefKeys {
    const val BACKEND_URL = "BACKEND_URL"
}
