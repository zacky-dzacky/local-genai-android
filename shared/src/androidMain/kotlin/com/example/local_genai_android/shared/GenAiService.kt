package com.example.local_genai_android.shared

import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.google.ai.edge.litertlm.Content
import com.google.ai.edge.litertlm.LiteRtlm
import com.google.ai.edge.litertlm.Message
import com.google.ai.edge.litertlm.TaskConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

actual class GenAiService(private val context: Context) {

    private var liteRtlm: LiteRtlm? = null
    private val coroutineScope = MainScope()

    init {
        coroutineScope.launch {
            try {
                // For this example, we'll use a placeholder task config.
                // In a real app, you would configure this with your model and other options.
                val taskConfig = TaskConfig.builder()
                    .setLlmModel(TaskConfig.LlmModel.GEMMA)
                    .build()
                liteRtlm = LiteRtlm.create(context, taskConfig)
            } catch (e: Exception) {
                // Handle initialization error
            }
        }
    }

    actual suspend fun processUserPrompt(
        prompt: String,
        tools: List<Tool>,
        onResult: (List<Action>) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.Default) {
            Log.d(TAG, "Start processing user prompt: $userPrompt")
            setProcessing(processing = true)
            setShowWelcomeMessage(showWelcomeMessage = false)

            // Clean up.
            setModelResponse(response = "")
            setNoFunctionRecognized(value = false)
            clearFunctionCallDetails()

            // Set user prompt.
            setUserPrompt(prompt = userPrompt)

            // Wait until the conversation is NOT resetting.
            Log.d(TAG, "Waiting for any ongoing conversation reset to be done...")
            isResettingConversation.first { !it }
            Log.d(TAG, "Done waiting. Start inference.")

            // Run inference.
            val instance = model.instance as LlmModelInstance
            val conversation = instance.conversation
            val contents = mutableListOf<Content>()
            if (userPrompt.trim().isNotEmpty()) {
                contents.add(Content.Text(userPrompt))
            }

            conversation
                .sendMessageAsync(Message.of(contents))
                .catch {
                    Log.e(TAG, "Failed to run inference", it)
                    onError(it.message ?: "Unknown error")
                }
                .onCompletion {
                    setProcessing(processing = false)
                    onProcessDone()
                    resetConversation(model = model, tools = tools)
                }
                .collect {
                    setProcessing(processing = false)
                    appendModelResponse(partialResponse = it.toString())
                }
        }
    }

    actual suspend fun performAction(action: Action): String {
        // In a real app, you would have a mapping of action names to actual functions.
        // For this example, we'll just return a success message.
        return "Action '${action.name}' executed successfully."
    }
}