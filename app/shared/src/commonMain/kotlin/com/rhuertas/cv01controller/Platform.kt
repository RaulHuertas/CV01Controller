package com.rhuertas.cv01controller

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform