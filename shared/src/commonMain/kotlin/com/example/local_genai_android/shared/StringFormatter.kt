package com.example.local_genai_android.shared

import com.example.local_genai_android.shared.Action

expect class StringFormatter {
    fun formatAction(action: Action): String
    fun getNoFunctionCallMessage(): String
}
