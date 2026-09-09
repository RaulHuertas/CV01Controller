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
    val name: String,
    val xPrecision: Float,
    val yPrecision: Float
)


data class LaserProject(
    val images: List<Image>,
    val laserSpecs: LaserSpecs = LaserSpecs(width = 170f,height=200f,name="CV01",xPrecision=0.1f,yPrecision=0.1f),
    val renderSettings: RenderSettings = RenderSettings( speed = 0.5f, power = 0.5f)
)