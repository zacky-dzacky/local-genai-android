package com.example.local_genai_android.shared

actual class Logger {
    actual fun d(tag: String, message: String) {
        println("DEBUG [$tag]: $message")
    }

    actual fun e(tag: String, message: String, throwable: Throwable?) {
        println("ERROR [$tag]: $message")
        throwable?.printStackTrace()
    }
}