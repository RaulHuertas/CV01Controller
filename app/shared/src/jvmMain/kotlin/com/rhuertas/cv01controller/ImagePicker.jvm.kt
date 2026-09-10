package com.rhuertas.cv01controller

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import java.io.File
import javax.imageio.ImageIO
import javax.swing.JFileChooser

private const val FallbackDpi = 72f

@Composable
actual fun rememberImagePicker(): ImagePicker = remember {
    object : ImagePicker {
        override fun pickImage(onImagePicked: (OriginalImageInfo?) -> Unit) {
            val chooser = JFileChooser()
            val result = chooser.showOpenDialog(null)
            if (result != JFileChooser.APPROVE_OPTION) {
                onImagePicked(null)
                return
            }

            val file = chooser.selectedFile
            val copiedFile = copyImageToAppStorage(file) ?: run {
                onImagePicked(null)
                return
            }
            val image = ImageIO.read(copiedFile)
            if (image == null) {
                copiedFile.delete()
                onImagePicked(null)
                return
            }

            val width = image.width.toFloat()
            val height = image.height.toFloat()
            onImagePicked(
                OriginalImageInfo(
                    physical_width = width * 25.4f / FallbackDpi,
                    physical_height = height * 25.4f / FallbackDpi,
                    pixelsW = width,
                    pixelsH = height,
                    uri = copiedFile.toURI().toString(),
                ),
            )
        }
    }
}

private fun copyImageToAppStorage(source: File): File? {
    val userHome = System.getProperty("user.home") ?: return null
    val targetDirectory = File(File(userHome, ".cv01controller"), "picked-images")
    if (!targetDirectory.exists() && !targetDirectory.mkdirs()) return null

    val extension = source.extension.takeIf { it.isNotBlank() } ?: "img"
    val targetFile = File(
        targetDirectory,
        "picked-${System.currentTimeMillis()}-${kotlin.random.Random.nextInt(1000, 9999)}.$extension",
    )

    return runCatching {
        source.inputStream().use { input ->
            targetFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        targetFile
    }.getOrNull()
}
