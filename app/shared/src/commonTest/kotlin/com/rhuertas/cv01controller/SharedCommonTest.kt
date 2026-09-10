package com.rhuertas.cv01controller

import androidx.compose.ui.geometry.Offset
import kotlin.test.Test
import kotlin.test.assertEquals

class SharedCommonTest {

    @Test
    fun example() {
        assertEquals(3, 1 + 2)
    }

    @Test
    fun workspaceDragDeltaConvertsPixelsToWorkspaceUnits() {
        val delta = workspaceDragDelta(
            dragAmount = Offset(x = 20f, y = 10f),
            laserSpecs = LaserWorkspaceSpecs(
                width = 200f,
                height = 100f,
                name = "test",
                xPrecision = 1f,
                yPrecision = 1f,
            ),
            canvasWidth = 400f,
            canvasHeight = 200f,
        )

        assertEquals(10f, delta.x)
        assertEquals(5f, delta.y)
    }

    @Test
    fun workspaceDragDeltaIsZeroWhenCanvasHasNoSize() {
        val delta = workspaceDragDelta(
            dragAmount = Offset(x = 20f, y = 10f),
            laserSpecs = LaserWorkspaceSpecs(
                width = 200f,
                height = 100f,
                name = "test",
                xPrecision = 1f,
                yPrecision = 1f,
            ),
            canvasWidth = 0f,
            canvasHeight = 0f,
        )

        assertEquals(0f, delta.x)
        assertEquals(0f, delta.y)
    }

    @Test
    fun workspaceDragDeltasCanBeAccumulatedAcrossPointerEvents() {
        val specs = LaserWorkspaceSpecs(
            width = 200f,
            height = 100f,
            name = "test",
            xPrecision = 1f,
            yPrecision = 1f,
        )
        val firstDelta = workspaceDragDelta(Offset(x = 4f, y = 0f), specs, 400f, 200f)
        val secondDelta = workspaceDragDelta(Offset(x = 6f, y = 0f), specs, 400f, 200f)

        assertEquals(5f, firstDelta.x + secondDelta.x)
    }
}
