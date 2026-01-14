package com.example.local_genai_android.shared.util

import com.zarief.litermlm.LiteRtLmSamplerParams


data class LLMModelInstance(val engine: LiteRtLmSamplerParams, var conversation: Conversation)

class LLMChatModelHelper {
    fun loadModel() {
    }
}