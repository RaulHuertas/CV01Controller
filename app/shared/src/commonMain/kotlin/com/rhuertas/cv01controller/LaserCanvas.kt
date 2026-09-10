package com.rhuertas.cv01controller

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.floor

@Composable
fun LaserCanvas(
    project: LaserProject,
    onProjectChange: (LaserProject) -> Unit,
    imagePicker: ImagePicker,
    modifier: Modifier = Modifier,
) {
    val gridSpacing = 10f

    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .safeContentPadding()
            .padding(16.dp),
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(project.laserSpecs.width / project.laserSpecs.height)
                .align(Alignment.CenterHorizontally),
        ) {
            Canvas(modifier = Modifier.matchParentSize()) {
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
            }

            project.target?.let { target ->
                val boxWidth = maxWidth
                val boxHeight = maxHeight
                val xFraction = target.workspaceArea.x / project.laserSpecs.width
                val yFraction =
                    (project.laserSpecs.height - target.workspaceArea.y - target.workspaceArea.height) /
                        project.laserSpecs.height
                val widthFraction = target.workspaceArea.width / project.laserSpecs.width
                val heightFraction = target.workspaceArea.height / project.laserSpecs.height

                LocalImage(
                    uri = target.originalImage.uri,
                    contentDescription = "Target image",
                    modifier = Modifier
                        .offset {
                            IntOffset(
                                x = (boxWidth * xFraction).roundToPx(),
                                y = (boxHeight * yFraction).roundToPx(),
                            )
                        }
                        .size(
                            width = boxWidth * widthFraction,
                            height = boxHeight * heightFraction,
                        ),
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = {
            imagePicker.pickImage { pickedImage ->
                if (pickedImage == null) return@pickImage

                val targetWidthRaw = pickedImage.physical_width
                val targetHeightRaw = pickedImage.physical_height
                var targetWidth = targetWidthRaw
                var targetHeight = targetHeightRaw
                if (targetWidthRaw > project.laserSpecs.width) {
                    targetWidth = project.laserSpecs.width
                    targetHeight = targetWidth * (pickedImage.pixelsH / pickedImage.pixelsW)
                } else if (targetHeightRaw > project.laserSpecs.height) {
                    targetHeight = project.laserSpecs.height
                    targetWidth = targetHeight * (pickedImage.pixelsW / pickedImage.pixelsH)
                }

                val x = (project.laserSpecs.width - targetWidth) / 2f
                val y = (project.laserSpecs.height - targetHeight) / 2f

                onProjectChange(
                    project.copy(
                        target = TargetImage(
                            originalImage = pickedImage,
                            workspaceArea = WorkspaceArea(
                                width = targetWidth,
                                height = targetHeight,
                                x = x,
                                y = y,
                            ),
                        ),
                    ),
                )
            }
        }) {
            Text("Load image")
        }
    }
}
