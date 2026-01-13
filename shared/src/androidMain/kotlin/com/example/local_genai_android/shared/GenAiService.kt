package com.example.local_genai_android.shared

import android.content.Context
import com.example.local_genai_android.shared.data.Model
import com.example.local_genai_android.shared.util.LlmModelInstance
import com.google.ai.edge.litertlm.Content
import com.google.ai.edge.litertlm.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch

actual class GenAiService(private val context: Context) {

    private val coroutineScope = MainScope()

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

        coroutineScope.launch(Dispatchers.Default) {


            // Run inference
            val instance = model.instance as LlmModelInstance
            val conversation = instance.conversation
            val contents = mutableListOf<Content>()
            if (prompt.trim().isNotEmpty()) {
                contents.add(Content.Text(prompt))
            }

            conversation
                .sendMessageAsync(Message.of(contents))
                .catch {
//                    Log.e(TAG, "Failed to run inference", it)
                    onError(it.message ?: "Unknown error")
                }
                .onCompletion {
//                    setProcessing(processing = false)
//                    onProcessDone()
//                    resetConversation(model = model, tools = tools)
                }
                .collect {
//                    setProcessing(processing = false)
//                    appendModelResponse(partialResponse = it.toString())
                }

        }
    }

    actual suspend fun performAction(action: Action): String {
        // In a real app, you would have a mapping of action names to actual functions.
        return "Action '${action.name}' executed successfully."
    }
}