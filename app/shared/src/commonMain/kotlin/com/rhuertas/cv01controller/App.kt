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
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import kotlin.math.floor

@Composable
@Preview
fun App() {
    MaterialTheme {
        val imagePicker = rememberImagePicker()
        val project = retain { LaserProject() }
        val gridSpacing = 10f
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                //.verticalScroll(rememberScrollState())
                .safeContentPadding(),
        ) {

            Canvas(
                modifier = Modifier.
                fillMaxWidth().
                aspectRatio(project.laserSpecs.width/project.laserSpecs.height)
                    .align(Alignment.CenterHorizontally)

            ) {

                drawRect(Color.White)
                val horizontalGridSpaces = floor(project.laserSpecs.width / gridSpacing).toInt()
                val verticalGridSpaces = floor(project.laserSpecs.height / gridSpacing).toInt()
                val gridYCanvasSpacing = size.height / verticalGridSpaces
                val gridXCanvasSpacing = size.width / horizontalGridSpaces
                for (i in 0 until verticalGridSpaces+1)
                {
                    val y = i * gridYCanvasSpacing
                    drawLine(
                        color = Color.LightGray,
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = 1f
                    )
                }
                for(i in 0 until horizontalGridSpaces+1)
                {
                    val x = i * gridXCanvasSpacing
                    drawLine(
                        color = Color.LightGray,
                        start = Offset(x, 0f),
                        end = Offset(x, size.height),
                        strokeWidth = 1f
                    )
                }
            }

        }
    }
}
