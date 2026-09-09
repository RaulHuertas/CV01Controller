package com.rhuertas.cv01controller

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.floor

@Composable
@Preview
fun App() {
    MaterialTheme {
        val imagePicker = rememberImagePicker()
        var project by remember { mutableStateOf(LaserProject()) }
        val gridSpacing = 10f

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .safeContentPadding()
                .padding(16.dp),
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(project.laserSpecs.width / project.laserSpecs.height)
                    .align(Alignment.CenterHorizontally),
            ) {
                drawRect(Color.White)

                val horizontalGridSpaces = floor(project.laserSpecs.width / gridSpacing).toInt()
                val verticalGridSpaces = floor(project.laserSpecs.height / gridSpacing).toInt()
                val gridYCanvasSpacing = size.height / verticalGridSpaces
                val gridXCanvasSpacing = size.width / horizontalGridSpaces

                for (i in 0..verticalGridSpaces) {
                    val y = i * gridYCanvasSpacing
                    drawLine(
                        color = Color.LightGray,
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = 1f,
                    )
                }

                for (i in 0..horizontalGridSpaces) {
                    val x = i * gridXCanvasSpacing
                    drawLine(
                        color = Color.LightGray,
                        start = Offset(x, 0f),
                        end = Offset(x, size.height),
                        strokeWidth = 1f,
                    )
                }

                project.target?.let { target ->
                    val scaleX = size.width / project.laserSpecs.width
                    val scaleY = size.height / project.laserSpecs.height
                    val imageScale = size.width / project.laserSpecs.width
                    val left = target.x * imageScale
                    val top = size.height - ((target.y + target.height) * imageScale)
                    val imageWidth = size.width/2
                    val imageHeight = imageWidth * (target.originalImage.pixelsH / target.originalImage.pixelsW)
                    drawRect(
                        color = Color(0xFF6C63FF),
                        topLeft = Offset(0f, 0f),
                        size = Size(imageWidth, imageHeight),
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = {
                imagePicker.pickImage { pickedImage ->
                    if (pickedImage == null) return@pickImage

                    val targetWidth = pickedImage.width.toFloat()
                    val targetHeight = pickedImage.height.toFloat()
                    val x = (project.laserSpecs.width - targetWidth) / 2f
                    val y = (project.laserSpecs.height - targetHeight) / 2f

                    project = project.copy(
                        target = TargetImage(
                            originalImage = pickedImage,
                            width = targetWidth,
                            height = targetHeight,
                            x = x,
                            y = y,
                            rotation = 0f,
                        ),
                    )
                }
            }) {
                Text("Load image")
            }
        }
    }
}
