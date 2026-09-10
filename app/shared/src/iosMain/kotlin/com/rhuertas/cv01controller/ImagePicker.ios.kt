package com.rhuertas.cv01controller

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
actual fun rememberImagePicker(): ImagePicker = remember {
    object : ImagePicker {
        override fun pickImage(onImagePicked: (OriginalImageInfo?) -> Unit) {
            onImagePicked(null)
        }
    }
}
