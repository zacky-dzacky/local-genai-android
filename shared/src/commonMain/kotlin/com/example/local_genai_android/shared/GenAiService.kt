package com.example.local_genai_android.shared

import com.example.local_genai_android.shared.data.Model
import dev.icerock.moko.mvvm.viewmodel.ViewModel

// A placeholder for a tool that the AI model can use.
data class Tool(val name: String)

// Represents an action that the AI model wants to perform.
data class Action(val name: String, val args: Map<String, String>)

expect class GenAiService {
    suspend fun processUserPrompt(
        model: Model,
        prompt: String,
        tools: List<Tool>,
        onResult: (List<Action>) -> Unit,
        onError: (String) -> Unit
    )

    suspend fun performAction(action: Action): String
}