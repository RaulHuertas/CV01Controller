package com.rhuertas.cv01controller

import androidx.compose.runtime.Composable

interface ImagePicker {
    fun pickImage(onImagePicked: (OriginalImageInfo?) -> Unit)
}

@Composable
expect fun rememberImagePicker(): ImagePicker
