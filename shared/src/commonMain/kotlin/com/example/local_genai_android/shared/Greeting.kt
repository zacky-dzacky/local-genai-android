package com.example.local_genai_android.shared

class Greeting {
    fun greet(): String {
        return "Hello, ${Platform().platform}!"
    }
}

expect class Platform() {
    val platform: String
}