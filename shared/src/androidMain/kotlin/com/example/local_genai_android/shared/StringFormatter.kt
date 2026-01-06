package com.example.local_genai_android.shared

import android.content.Context

actual class StringFormatter(private val context: Context) {
    actual fun formatAction(action: Action): String {
        // This is a simplified version of the original genFormattedFunctionCall.
        // In a real app, you would use string resources for localization.
        val argsString = action.args.entries.joinToString { "${it.key}: ${it.value}" }
        return "Called function '${action.name}' with arguments: $argsString"
    }

    actual fun getNoFunctionCallMessage(): String {
        // This would typically come from a string resource.
        return "Sorry, I don't know how to help with that."
    }
}
