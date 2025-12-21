package com.sunday.baseassist

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform