package com.example.local_genai_android.shared.util

import android.content.Context
import android.util.Log
import com.example.local_genai_android.shared.data.Accelerator
import com.example.local_genai_android.shared.data.ConfigKeys
import com.example.local_genai_android.shared.data.DEFAULT_TOPK
import com.example.local_genai_android.shared.data.DEFAULT_MAX_TOKEN
import com.example.local_genai_android.shared.data.DEFAULT_TEMPERATURE
import com.example.local_genai_android.shared.data.DEFAULT_TOPP
import com.example.local_genai_android.shared.data.Model
import com.google.ai.edge.litertlm.Backend
import com.google.ai.edge.litertlm.Conversation
import com.google.ai.edge.litertlm.ConversationConfig
import com.google.ai.edge.litertlm.Engine
import com.google.ai.edge.litertlm.EngineConfig
import com.google.ai.edge.litertlm.ExperimentalApi
import com.google.ai.edge.litertlm.ExperimentalFlags
import com.google.ai.edge.litertlm.Message
import com.google.ai.edge.litertlm.SamplerConfig

private const val TAG = "AGLlmChatModelHelper"

typealias ResultListener = (partialResult: String, done: Boolean) -> Unit

typealias CleanUpListener = () -> Unit

data class LlmModelInstance(val engine: Engine, var conversation: Conversation)

object LLMChatModelHelper {
    private val cleanUpListeners: MutableMap<String, CleanUpListener> = mutableMapOf()

    @OptIn(ExperimentalApi::class) // opt-in experimental flags
    fun initialize(
        context: Context,
        model: Model,
        supportImage: Boolean,
        supportAudio: Boolean,
        onDone: (String) -> Unit,
        systemMessage: Message? = null,
        tools: List<Any> = listOf(),
        enableConversationConstrainedDecoding: Boolean = false,
    ) {
        // Prepare options.
        val maxTokens =
            model.getIntConfigValue(key = ConfigKeys.MAX_TOKENS, defaultValue = DEFAULT_MAX_TOKEN)
        val topK = model.getIntConfigValue(key = ConfigKeys.TOPK, defaultValue = DEFAULT_TOPK)
        val topP = model.getFloatConfigValue(key = ConfigKeys.TOPP, defaultValue = DEFAULT_TOPP)
        val temperature =
            model.getFloatConfigValue(key = ConfigKeys.TEMPERATURE, defaultValue = DEFAULT_TEMPERATURE)
        val accelerator =
            model.getStringConfigValue(key = ConfigKeys.ACCELERATOR, defaultValue = Accelerator.GPU.label)
        Log.d(TAG, "Initializing...")
        Log.d(TAG, "Enable image: $supportImage, enable audio: $supportAudio")
        val preferredBackend =
            when (accelerator) {
                Accelerator.CPU.label -> Backend.CPU
                Accelerator.GPU.label -> Backend.GPU
                else -> Backend.CPU
            }
        Log.d(TAG, "Preferred backend: $preferredBackend")

        val modelPath = model.getPath(context = context)
        val engineConfig =
            EngineConfig(
                modelPath = modelPath,
                backend = preferredBackend,
                visionBackend = if (supportImage) Backend.GPU else null, // must be GPU for Gemma 3n
                audioBackend = if (supportAudio) Backend.CPU else null, // must be CPU for Gemma 3n
                maxNumTokens = maxTokens,
                cacheDir =
                    if (modelPath.startsWith("/data/local/tmp"))
                        context.getExternalFilesDir(null)?.absolutePath
                    else null,
            )

        // Create an instance of LiteRT LM engine and conversation.
        try {
            val engine = Engine(engineConfig)
            engine.initialize()

            ExperimentalFlags.enableConversationConstrainedDecoding =
                enableConversationConstrainedDecoding
            val conversation =
                engine.createConversation(
                    ConversationConfig(
                        samplerConfig =
                            SamplerConfig(
                                topK = topK,
                                topP = topP.toDouble(),
                                temperature = temperature.toDouble(),
                            ),
                        systemMessage = systemMessage,
                        tools = tools,
                    )
                )
            ExperimentalFlags.enableConversationConstrainedDecoding = false
            model.instance = LlmModelInstance(engine = engine, conversation = conversation)
        } catch (e: Exception) {
            onDone(cleanUpMediapipeTaskErrorMessage(e.message ?: "Unknown error"))
            return
        }
        onDone("")
    }
}