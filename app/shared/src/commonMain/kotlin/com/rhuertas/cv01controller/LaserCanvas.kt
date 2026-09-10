package com.rhuertas.cv01controller

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.floor

private const val TAG = "LaserCanvas"

@Composable
fun LaserCanvas(
    project: LaserProject,
    updateProject: (LaserProject) -> Unit,
    imagePicker: ImagePicker,
    modifier: Modifier = Modifier,
) {
    val gridSpacing = 10f
    val dragX = remember { mutableStateOf(0f) }
    val dragY = remember { mutableStateOf(0f) }
    var widthInput by remember(project.target?.workspaceArea?.width) {
        mutableStateOf(project.target?.workspaceArea?.width?.formatWorkspaceValue().orEmpty())
    }
    var heightInput by remember(project.target?.workspaceArea?.height) {
        mutableStateOf(project.target?.workspaceArea?.height?.formatWorkspaceValue().orEmpty())
    }
    var xInput by remember(project.target?.workspaceArea?.x) {
        mutableStateOf(project.target?.workspaceArea?.x?.formatWorkspaceValue().orEmpty())
    }
    var yInput by remember(project.target?.workspaceArea?.y) {
        mutableStateOf(project.target?.workspaceArea?.y?.formatWorkspaceValue().orEmpty())
    }
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .safeContentPadding()
            .imePadding()
            .verticalScroll(rememberScrollState())
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
                val latestProject by rememberUpdatedState(project)
                val latestUpdateProject by rememberUpdatedState(updateProject)
                val canvasWidth = maxWidth
                val canvasHeight = maxHeight
                val xFraction = target.workspaceArea.x / project.laserSpecs.width
                val yFraction =
                    (project.laserSpecs.height - target.workspaceArea.y - target.workspaceArea.height) /
                        project.laserSpecs.height
                val widthFraction = target.workspaceArea.width / project.laserSpecs.width
                val heightFraction = target.workspaceArea.height / project.laserSpecs.height

                Box(
                    modifier = Modifier
                        .offset {
                            IntOffset(
                                //x = (canvasWidth * xFraction).roundToPx(),
                                //y = (canvasHeight * yFraction).roundToPx(),
                                x = (canvasWidth.toPx() * target.workspaceArea.x/project.laserSpecs.width).toInt(),
                                y = (canvasHeight.toPx() *((project.laserSpecs.height+target.workspaceArea.y-target.workspaceArea.height)/project.laserSpecs.height)).toInt(),
                                //y = (canvasHeight.value *((project.laserSpecs.height+target.workspaceArea.height)/project.laserSpecs.height)).toInt(),
                                //y = 0
                            )
                        }
                        .size(
                            //width = canvasWidth * widthFraction,
                            //height = canvasHeight * heightFraction,
                            width =  (canvasWidth * target.workspaceArea.width/project.laserSpecs.width),
                            height = (canvasHeight * target.workspaceArea.height/project.laserSpecs.height),
                        )
                        .pointerInput(project.laserSpecs, canvasWidth, canvasHeight) {
                            var accumulatedX = target.workspaceArea.x
                            var accumulatedY = target.workspaceArea.y
                            detectDragGestures(
                                onDragStart = {
                                    latestProject.target?.workspaceArea?.let { currentArea ->
                                        accumulatedX = currentArea.x
                                        accumulatedY = currentArea.y
                                    }
                                },
                            ) { change, dragAmount ->
                                change.consume()
                                dragX.value = dragAmount.x
                                dragY.value = dragAmount.y
                                val (deltaX, deltaY) = workspaceDragDelta(
                                    dragAmount = dragAmount,
                                    laserSpecs = project.laserSpecs,
                                    canvasWidth = canvasWidth.value,
                                    canvasHeight = canvasHeight.value,
                                )
                                accumulatedX += deltaX
                                accumulatedY += deltaY
                                val updatedArea = normalizeWorkspacePosition(
                                    current = target.workspaceArea,
                                    laserSpecs = project.laserSpecs,
                                    x = accumulatedX,
                                    y = accumulatedY,
                                )

                                latestUpdateProject(
                                    latestProject.withWorkspaceArea(updatedArea),
                                )
                            }
                        }
                ) {
                    LocalImage(
                        uri = target.originalImage.uri,
                        contentDescription = "Target image",
                        modifier = Modifier.matchParentSize(),
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text("Drag X: ${dragX.value}, Drag Y: ${dragY.value}")
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

                updateProject(
                    project.copy(
                        target = TargetImage(
                            originalImage = pickedImage,
                            workspaceArea = WorkspaceArea(
                                width = targetWidth,
                                height = targetHeight,
                                //x = x,
                                //y = y,
                                x = 0f,
                                y = 0f
             ),
                        ),
                    ),
                )
            }
        }) {
            Text("Load image")
        }

        project.target?.let { target ->
            Spacer(modifier = Modifier.height(12.dp))

            WorkspaceFieldRow(
                firstLabel = "Width",
                firstValue = widthInput,
                onFirstValueChange = { newValue ->
                    widthInput = newValue
                    newValue.toFloatOrNull()?.let { width ->
                        val updatedArea = normalizeWorkspaceArea(
                            current = target.workspaceArea,
                            laserSpecs = project.laserSpecs,
                            width = width,
                        )
                        updateProject(project.withWorkspaceArea(updatedArea))
                    }
                },
                secondLabel = "Height",
                secondValue = heightInput,
                onSecondValueChange = { newValue ->
                    heightInput = newValue
                    newValue.toFloatOrNull()?.let { height ->
                        val updatedArea = normalizeWorkspaceArea(
                            current = target.workspaceArea,
                            laserSpecs = project.laserSpecs,
                            height = height,
                        )
                        updateProject(project.withWorkspaceArea(updatedArea))
                    }
                },
            )

            Spacer(modifier = Modifier.height(8.dp))

            WorkspaceFieldRow(
                firstLabel = "X",
                firstValue = xInput,
                onFirstValueChange = { newValue ->
                    xInput = newValue
                    newValue.toFloatOrNull()?.let { x ->
                        val updatedArea = normalizeWorkspaceArea(
                            current = target.workspaceArea,
                            laserSpecs = project.laserSpecs,
                            x = x,
                        )
                        updateProject(project.withWorkspaceArea(updatedArea))
                    }
                },
                secondLabel = "Y",
                secondValue = yInput,
                onSecondValueChange = { newValue ->
                    yInput = newValue
                    newValue.toFloatOrNull()?.let { y ->
                        val updatedArea = normalizeWorkspaceArea(
                            current = target.workspaceArea,
                            laserSpecs = project.laserSpecs,
                            y = y,
                        )
                        updateProject(project.withWorkspaceArea(updatedArea))
                    }
                },
            )
        }
    }
}

@Composable
private fun WorkspaceFieldRow(
    firstLabel: String,
    firstValue: String,
    onFirstValueChange: (String) -> Unit,
    secondLabel: String,
    secondValue: String,
    onSecondValueChange: (String) -> Unit,
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = firstValue,
            onValueChange = onFirstValueChange,
            label = { Text(firstLabel) },
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
            singleLine = true,
        )
        OutlinedTextField(
            value = secondValue,
            onValueChange = onSecondValueChange,
            label = { Text(secondLabel) },
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
            singleLine = true,
        )
    }
}

private fun LaserProject.withWorkspaceArea(workspaceArea: WorkspaceArea): LaserProject =
    copy(
        target = target?.copy(workspaceArea = workspaceArea),
    )

private fun normalizeWorkspaceArea(
    current: WorkspaceArea,
    laserSpecs: LaserWorkspaceSpecs,
    width: Float = current.width,
    height: Float = current.height,
    x: Float = current.x,
    y: Float = current.y,
): WorkspaceArea {
    val clampedWidth = width.coerceIn(0f, laserSpecs.width)
    val clampedHeight = height.coerceIn(0f, laserSpecs.height)
    //val clampedX = x.coerceIn(0f, laserSpecs.width - clampedWidth)
    //val clampedY = y.coerceIn(0f, laserSpecs.height - clampedHeight)
    val clampedX = x.coerceIn(-clampedWidth, laserSpecs.width )
    val clampedY = y.coerceIn(-clampedHeight, laserSpecs.height )
    return WorkspaceArea(
        width = clampedWidth,
        height = clampedHeight,
        x = clampedX,
        y = clampedY,
    )

}

private fun Float.formatWorkspaceValue(): String =
    if (this % 1f == 0f) {
        toInt().toString()
    } else {
        toString()
    }
