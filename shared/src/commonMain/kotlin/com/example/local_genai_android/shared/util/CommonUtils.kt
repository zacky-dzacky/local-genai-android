package com.example.local_genai_android.shared.util

private const val TAG = "AGUtils"

fun cleanUpMediapipeTaskErrorMessage(message: String): String {
    val index = message.indexOf("=== Source Location Trace")
    if (index >= 0) {
        return message.substring(0, index)
    }
    return message
}

fun processLlmResponse(response: String): String {
    return response.replace("\\n", "\n")
}