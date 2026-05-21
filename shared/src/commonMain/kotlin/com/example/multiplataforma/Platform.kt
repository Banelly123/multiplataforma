package com.example.multiplataforma

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform