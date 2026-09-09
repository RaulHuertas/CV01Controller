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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
@Preview
fun App() {
    MaterialTheme {
        val specs = LaserWorkspaceSpecs(width = 170f, height = 200f, name = "CV01", xPrecision = 0.1f, yPrecision = 0.1f)
        var project by remember { mutableStateOf(LaserProject(images = listOf(sampleImage()), laserSpecs = specs)) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
        ) {
            Text(text = "Laser workspace: ${project.laserSpecs.name}")
            Spacer(modifier = Modifier.height(12.dp))

            Canvas(
                modifier = Modifier
                    .size(320.dp, 320.dp * (project.laserSpecs.height / project.laserSpecs.width))
            ) {
                val scaleX = size.width / project.laserSpecs.width
                val scaleY = size.height / project.laserSpecs.height

                drawRect(Color.White)
                var currentX = 0f
                while (currentX <= size.width) {
                    drawLine(Color(0xFFE0E0E0), Offset(currentX, 0f), Offset(currentX, size.height), strokeWidth = 1f)
                    currentX += project.laserSpecs.xPrecision * scaleX
                }
                var currentY = 0f
                while (currentY <= size.height) {
                    drawLine(Color(0xFFE0E0E0), Offset(0f, currentY), Offset(size.width, currentY), strokeWidth = 1f)
                    currentY += project.laserSpecs.yPrecision * scaleY
                }

                project.images.forEachIndexed { index, image ->
                    val left = image.x * scaleX
                    val top = size.height - ((image.y + image.height) * scaleY)
                    val imageWidth = image.width * scaleX
                    val imageHeight = image.height * scaleY

                    drawRect(
                        color = if (index == 0) Color(0xFF6C63FF) else Color(0xFFB39DDB),
                        topLeft = Offset(left, top),
                        size = androidx.compose.ui.geometry.Size(imageWidth, imageHeight),
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                project = project.copy(
                    images = project.images + sampleImage().copy(
                        x = 20f,
                        y = 20f,
                    ),
                )
            }) {
                Text("Load image")
            }
        }
    }
}
