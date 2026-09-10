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
            dragXPixels = 20f,
            dragYPixels = 10f,
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
            dragXPixels = 20f,
            dragYPixels = 10f,
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
        val firstDelta = workspaceDragDelta(dragXPixels = 4f, dragYPixels = 0f, laserSpecs = specs, canvasWidth = 400f, canvasHeight = 200f)
        val secondDelta = workspaceDragDelta(dragXPixels = 6f, dragYPixels = 0f, laserSpecs = specs, canvasWidth = 400f, canvasHeight = 200f)

        assertEquals(5f, firstDelta.x + secondDelta.x)
    }

    @Test
    fun normalizeWorkspacePositionPreservesCurrentSizeWhileMoving() {
        val current = WorkspaceArea(
            width = 120f,
            height = 80f,
            x = 10f,
            y = 20f,
        )

        val updated = normalizeWorkspacePosition(
            current = current,
            laserSpecs = LaserWorkspaceSpecs(
                width = 200f,
                height = 100f,
                name = "test",
                xPrecision = 1f,
                yPrecision = 1f,
            ),
            x = 30f,
            y = 40f,
        )

        assertEquals(120f, updated.width)
        assertEquals(80f, updated.height)
        assertEquals(30f, updated.x)
        assertEquals(40f, updated.y)
    }
}
