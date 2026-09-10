package com.rhuertas.cv01controller

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import android.graphics.BitmapFactory

@Composable
actual fun LocalImage(
    uri: String,
    contentDescription: String?,
    modifier: Modifier,
) {
    val context = LocalContext.current
    val image = produceState<ImageBitmap?>(initialValue = null, uri) {
        value = context.contentResolver.openInputStream(Uri.parse(uri))?.use { inputStream ->
            BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
        }
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
