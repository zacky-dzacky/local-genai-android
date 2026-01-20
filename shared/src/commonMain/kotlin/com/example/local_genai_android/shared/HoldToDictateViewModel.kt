package com.example.local_genai_android.shared

import com.example.local_genai_android.shared.platform.AppContext
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.StateFlow

data class HoldToDictateUiState(val recognizing: Boolean = false, val recognizedText: String = "")

expect class HoldToDictateViewModel(appContext: AppContext) : ViewModel {
    val uiState: StateFlow<HoldToDictateUiState>

    fun startSpeechRecognition(onDone: (String) -> Unit, onAmplitudeChanged: (Int) -> Unit)

    fun stopSpeechRecognition()

    fun cancelSpeechRecognition()

    fun setRecognizing(recognizing: Boolean)

    fun setRecognizedText(text: String)
}
