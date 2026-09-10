package com.rhuertas.cv01controller

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toFile

@Composable
actual fun LocalImage(
    uri: String,
    contentDescription: String?,
    modifier: Modifier,
) {
    val context = LocalContext.current
    val image = produceState<ImageBitmap?>(initialValue = null, uri) {
        val parsedUri = Uri.parse(uri)
        value = when (parsedUri.scheme) {
            "file" -> runCatching { BitmapFactory.decodeFile(parsedUri.toFile().absolutePath) }.getOrNull()
            else -> context.contentResolver.openInputStream(parsedUri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream)
            }
        }?.asImageBitmap()
    }

    image.value?.let { bitmap ->
        Image(
            bitmap = bitmap,
            contentDescription = contentDescription,
            contentScale = ContentScale.FillBounds,
            modifier = modifier,
        )
    }
}
