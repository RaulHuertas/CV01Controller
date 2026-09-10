package com.rhuertas.cv01controller

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.browser.document
import org.w3c.dom.HTMLImageElement
import org.w3c.dom.HTMLInputElement
import org.w3c.files.File
import org.w3c.files.FileReader
import kotlin.js.ExperimentalWasmJsInterop

private const val FallbackDpi = 72f

@Composable
actual fun rememberImagePicker(): ImagePicker = remember {
    object : ImagePicker {
        override fun pickImage(onImagePicked: (OriginalImageInfo?) -> Unit) {
            val input = document.createElement("input") as HTMLInputElement
            input.type = "file"
            input.accept = "image/*"
            input.addEventListener("change", { _ ->
                val file = input.files?.item(0)
                if (file == null) {
                    onImagePicked(null)
                } else {
                    readFileAsDataUrl(file) { dataUrl ->
                        if (dataUrl == null) {
                            onImagePicked(null)
                            return@readFileAsDataUrl
                        }
                        val image = document.createElement("img") as HTMLImageElement
                        image.addEventListener("load", { _ ->
                            val width = image.naturalWidth.takeIf { it > 0 } ?: image.width
                            val height = image.naturalHeight.takeIf { it > 0 } ?: image.height
                            if (width <= 0 || height <= 0) {
                                onImagePicked(null)
                                return@addEventListener
                            }
                            onImagePicked(
                                OriginalImageInfo(
                                    physical_width = width * 25.4f / FallbackDpi,
                                    physical_height = height * 25.4f / FallbackDpi,
                                    pixelsW = width.toFloat(),
                                    pixelsH = height.toFloat(),
                                    uri = dataUrl,
                                ),
                            )
                        })
                        image.addEventListener("error", { _ ->
                            onImagePicked(null)
                        })
                        image.src = dataUrl
                    }
                }
            })
            input.click()
        }
    }
}

@OptIn(ExperimentalWasmJsInterop::class)
private fun readFileAsDataUrl(file: File, onResult: (String?) -> Unit) {
    val reader = FileReader()
    reader.addEventListener("load", { _ ->
        onResult(reader.result as? String)
    })
    reader.addEventListener("error", { _ ->
        onResult(null)
    })
    reader.readAsDataURL(file)
}
