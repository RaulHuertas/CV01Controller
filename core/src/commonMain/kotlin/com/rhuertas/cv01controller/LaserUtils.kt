package com.rhuertas.cv01controller

data class OriginalImage(
    val width: Int,
    val height: Int,
    val pixelsN: Float,
    val pixelsH: Float, // Assuming pixels are represented as a 2D list of integers (e.g., grayscale values)
    val uri: String
)

data class Image(
    val originalImage: OriginalImage,
    val width: Float,
    val height: Float,
    val x: Float,
    val y: Float
)

data class RenderSettings(
    val speed: Float,
    val power: Float,
)

data class LaserSpecs(
    val width: Float,
    val height: Float,
    val name: String
)


data class LaserProject(
    val images: List<Image>,
    val LaserSpecs: LaserSpecs,
    val renderSettings: RenderSettings
)