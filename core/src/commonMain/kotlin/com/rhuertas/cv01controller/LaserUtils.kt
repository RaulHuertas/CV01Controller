package com.rhuertas.cv01controller

data class OriginalImage(
    //physical dimensions of the image in millimeters
    val width: Int,
    val height: Int,
    //number of pixels in the image
    val pixelsN: Float,
    val pixelsH: Float,
    //path to the image file, can be a local path or a URL
    val uri: String
)

data class Image(
    val originalImage: OriginalImage,
    //Display parameters
    val width: Float,
    val height: Float,
    val x: Float,
    val y: Float,
    val rotation: Float
)

data class RenderSettings(
    //Laser operation parameters
    val speed: Float,
    val power: Float,
)

data class LaserWorkspaceSpecs(
    val width: Float,
    val height: Float,
    val name: String,
    val xPrecision: Float,
    val yPrecision: Float
)


data class LaserProject(
    val images: List<Image>,
    val laserSpecs: LaserWorkspaceSpecs = LaserWorkspaceSpecs(width = 170f,height=200f,name="CV01",xPrecision=0.1f,yPrecision=0.1f),
    val renderSettings: RenderSettings = RenderSettings( speed = 0.5f, power = 0.5f)
)