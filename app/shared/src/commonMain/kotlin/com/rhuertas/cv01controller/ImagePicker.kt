package com.rhuertas.cv01controller

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

interface ImagePicker {
    fun pickImage(onImagePicked: (OriginalImageInfo?) -> Unit)
}

@Composable
expect fun rememberImagePicker(): ImagePicker

@Composable
expect fun LocalImage(
    uri: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
)
