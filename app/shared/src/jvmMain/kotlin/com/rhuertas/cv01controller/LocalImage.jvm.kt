package com.rhuertas.cv01controller

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import java.io.File
import java.net.URI
import javax.imageio.ImageIO

@Composable
actual fun LocalImage(
    uri: String,
    contentDescription: String?,
    modifier: Modifier,
) {
    val image = produceState<ImageBitmap?>(initialValue = null, uri) {
        value = runCatching {
            ImageIO.read(File(URI(uri)))?.asImageBitmap()
        }.getOrNull()
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
