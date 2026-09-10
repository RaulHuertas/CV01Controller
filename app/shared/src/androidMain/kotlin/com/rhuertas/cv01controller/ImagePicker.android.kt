package com.rhuertas.cv01controller

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import java.io.File

private const val FallbackDpi = 72f
private const val PickedImagesDirectory = "picked-images"

@Composable
actual fun rememberImagePicker(): ImagePicker {
    val context = LocalContext.current
    var pendingCallback by remember { mutableStateOf<((OriginalImageInfo?) -> Unit)?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        val picked = uri?.let { selectedUri ->
            copyImageToAppStorage(context = context, sourceUri = selectedUri)?.let { copiedFile ->
                val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
                BitmapFactory.decodeFile(copiedFile.absolutePath, options)
                val width = options.outWidth
                val height = options.outHeight
                if (width <= 0 || height <= 0) {
                    copiedFile.delete()
                    return@let null
                }
                val dpi = options.inDensity.takeIf { it > 0 }?.toFloat() ?: FallbackDpi
                OriginalImageInfo(
                    physical_width = width * 25.4f / dpi,
                    physical_height = height * 25.4f / dpi,
                    pixelsW = width.toFloat(),
                    pixelsH = height.toFloat(),
                    uri = Uri.fromFile(copiedFile).toString(),
                )
            }
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

private fun copyImageToAppStorage(context: Context, sourceUri: Uri): File? {
    val targetDirectory = File(context.filesDir, PickedImagesDirectory)
    if (!targetDirectory.exists() && !targetDirectory.mkdirs()) return null

    val extension = context.contentResolver.getType(sourceUri)
        ?.let { mimeType -> MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType) }
        ?.takeIf { it.isNotBlank() }
        ?: "img"

    val targetFile = File(
        targetDirectory,
        "picked-${System.currentTimeMillis()}-${kotlin.random.Random.nextInt(1000, 9999)}.$extension",
    )
    val copied = context.contentResolver.openInputStream(sourceUri)?.use { input ->
        targetFile.outputStream().use { output -> input.copyTo(output) }
        true
    } ?: false
    if (!copied) return null
    return targetFile
}
