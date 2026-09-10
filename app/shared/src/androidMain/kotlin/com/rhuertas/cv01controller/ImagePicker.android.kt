package com.rhuertas.cv01controller

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun rememberImagePicker(): ImagePicker {
    val context = LocalContext.current
    var pendingCallback by remember { mutableStateOf<((OriginalImageInfo?) -> Unit)?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        val picked = uri?.let { selectedUri ->
            val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            context.contentResolver.openInputStream(selectedUri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream, null, options)
            }

            val width = options.outWidth.coerceAtLeast(1)
            val height = options.outHeight.coerceAtLeast(1)
            val dpi = options.inDensity.takeIf { it > 0 }?.toFloat() ?: 72f
            OriginalImageInfo(
                physical_width = width * 25.4f / dpi,
                physical_height = height * 25.4f / dpi,
                pixelsW = width.toFloat(),
                pixelsH = height.toFloat(),
                uri = selectedUri.toString(),
            )
        }

        pendingCallback?.invoke(picked)
        pendingCallback = null
    }

    return remember {
        object : ImagePicker {
            override fun pickImage(onImagePicked: (OriginalImageInfo?) -> Unit) {
                pendingCallback = onImagePicked
                launcher.launch("image/*")
            }
        }
    }
}
