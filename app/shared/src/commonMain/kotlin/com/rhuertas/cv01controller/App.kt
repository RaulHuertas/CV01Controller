package com.rhuertas.cv01controller

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
@Preview
fun App() {
    MaterialTheme {
        val imagePicker = rememberImagePicker()
        val specs = LaserWorkspaceSpecs(width = 170f, height = 200f, name = "CV01", xPrecision = 0.1f, yPrecision = 0.1f)
        var selectedImage by remember { mutableStateOf<OriginalImage?>(null) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
        ) {
            Text(text = "Laser workspace: ${specs.name}")
            Spacer(modifier = Modifier.height(12.dp))

            Canvas(
                modifier = Modifier
                    .size(320.dp, 320.dp * (specs.height / specs.width))
            ) {
                val scaleX = size.width / specs.width
                val scaleY = size.height / specs.height

                drawRect(Color.White)

                var currentX = 0f
                while (currentX <= size.width) {
                    drawLine(Color(0xFFE0E0E0), Offset(currentX, 0f), Offset(currentX, size.height), strokeWidth = 1f)
                    currentX += specs.xPrecision * scaleX
                }

                var currentY = 0f
                while (currentY <= size.height) {
                    drawLine(Color(0xFFE0E0E0), Offset(0f, currentY), Offset(size.width, currentY), strokeWidth = 1f)
                    currentY += specs.yPrecision * scaleY
                }

                val image = selectedImage
                if (image != null) {
                    val left = 10f
                    val top = 10f
                    val width = (image.width.coerceAtLeast(1).toFloat() * 0.8f) * scaleX
                    val height = (image.height.coerceAtLeast(1).toFloat() * 0.8f) * scaleY

                    drawRect(
                        color = Color(0xFF6C63FF),
                        topLeft = Offset(left, top),
                        size = Size(width.coerceAtMost(size.width - left), height.coerceAtMost(size.height - top)),
                    )
                    drawRect(
                        color = Color(0x66000000),
                        topLeft = Offset(left + 6f, top + 6f),
                        size = Size(width.coerceAtMost(size.width - left - 12f), height.coerceAtMost(size.height - top - 12f)),
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                imagePicker.pickImage { pickedImage -> selectedImage = pickedImage }
            }) {
                Text("Load image")
            }

            if (selectedImage != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "Loaded image URI: ${selectedImage!!.uri}")
            }
        }
    }
}
