package com.tavrida.energysales

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tavrida.utils.println
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.reflect.typeOf

@RunWith(AndroidJUnit4::class)
class MutableStateTest {
    @Test
    fun DoIt(){
        val state = mutableStateOf(123)
        state::class.java.println()
        state.value = 456
        state
    }
}