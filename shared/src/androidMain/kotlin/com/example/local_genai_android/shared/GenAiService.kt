package com.example.local_genai_android.shared

import android.content.Context
import com.example.local_genai_android.shared.data.Model
import com.google.ai.edge.litertlm.LiteRtlm
import com.google.ai.edge.litertlm.TaskConfig
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

actual class GenAiService(private val context: Context) {

    private var liteRtlm: LiteRtlm? = null
    private val coroutineScope = MainScope()

    init {
        coroutineScope.launch {
            try {
                val taskConfig = TaskConfig.builder()
                    .setLlmModel(TaskConfig.LlmModel.GEMMA)
                    .build()
                liteRtlm = LiteRtlm.create(context, taskConfig)
            } catch (e: Exception) {
                // Initialization errors will be handled when processUserPrompt is called.
            }
        }
    }

    actual suspend fun processUserPrompt(
        model: Model,
        prompt: String,
        tools: List<Tool>,
        onResult: (List<Action>) -> Unit,
        onError: (String) -> Unit
    ) {


        if (model.instance == null) {
            return
        }

        coroutineScope.launch {  }

        val service = liteRtlm
        if (service == null) {
            onError("AI Service not initialized.")
            return
        }

        try {
            val llmResult = service.process(prompt)
            if (llmResult != null) {
                val actions = llmResult.actions().map { Action(it.functionName(), it.args()) }
                onResult(actions)
            } else {
                onResult(emptyList())
            }
        } catch (e: Exception) {
            onError(e.message ?: "An unknown error occurred during processing.")
        }
    }

    actual suspend fun performAction(action: Action): String {
        // In a real app, you would have a mapping of action names to actual functions.
        return "Action '${action.name}' executed successfully."
    }
}