package com.rhuertas.cv01controller

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import javax.imageio.ImageIO
import javax.swing.JFileChooser

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
            val image = ImageIO.read(file)
            if (image == null) {
                onImagePicked(null)
                return
            }

            onImagePicked(
                OriginalImageInfo(
                    width = image.width,
                    height = image.height,
                    pixelsN = (image.width * image.height).toFloat(),
                    pixelsH = image.height.toFloat(),
                    uri = file.toURI().toString(),
                ),
            )
        }
    }
}
