package com.example.local_genai_android.shared

import com.example.local_genai_android.shared.Action

actual class StringFormatter {
    actual fun formatAction(action: Action): String {
        // Placeholder for iOS implementation
        val argsString = action.args.entries.joinToString { "${it.key}: ${it.value}" }
        return "Called function '${action.name}' with arguments: $argsString"
    }

    actual fun getNoFunctionCallMessage(): String {
        // Placeholder for iOS implementation
        return "Sorry, I don't know how to help with that."
    }
}
