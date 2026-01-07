package com.example.local_genai_android.shared

actual class GenAiService {
    actual suspend fun processUserPrompt(
        prompt: String,
        tools: List<Tool>,
        onResult: (List<Action>) -> Unit,
        onError: (String) -> Unit
    ) {
        // Placeholder for iOS implementation
        onError("AI Service not implemented on this platform yet.")
    }

    actual suspend fun performAction(action: Action): String {
        // Placeholder for iOS implementation
        return "Action performing not implemented on this platform yet."
    }
}