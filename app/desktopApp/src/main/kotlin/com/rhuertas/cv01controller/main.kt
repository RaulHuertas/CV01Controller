package com.rhuertas.cv01controller

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "CV01Controller",
    ) {
        App()
    }
}