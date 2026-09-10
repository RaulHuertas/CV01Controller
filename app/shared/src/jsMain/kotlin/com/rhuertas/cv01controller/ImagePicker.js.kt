package com.rhuertas.cv01controller

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.browser.document
import org.w3c.dom.HTMLImageElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.url.URL

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
                    val localUrl = URL.createObjectURL(file)
                    val image = document.createElement("img") as HTMLImageElement
                    image.addEventListener("load", { _ ->
                        onImagePicked(
                            OriginalImageInfo(
                                width = image.width,
                                height = image.height,
                                pixelsN = (image.width * image.height).toFloat(),
                                pixelsH = image.height.toFloat(),
                                uri = localUrl,
                            ),
                        )
                    })
                    image.addEventListener("error", { _ ->
                        onImagePicked(null)
                    })
                    image.src = localUrl
                }
            })
            input.click()
        }
    }
}
