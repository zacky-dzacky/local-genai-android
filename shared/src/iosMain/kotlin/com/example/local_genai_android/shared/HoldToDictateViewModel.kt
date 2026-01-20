package com.example.local_genai_android.shared

import com.example.local_genai_android.shared.platform.AppContext
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

actual class HoldToDictateViewModel actual constructor(appContext: AppContext) : ViewModel() {
    private val _uiState = MutableStateFlow(HoldToDictateUiState())
    actual val uiState = _uiState.asStateFlow()

    actual fun startSpeechRecognition(onDone: (String) -> Unit, onAmplitudeChanged: (Int) -> Unit) {
        // Placeholder for iOS speech recognition
    }

    actual fun stopSpeechRecognition() {
        // Placeholder for iOS speech recognition
    }

    actual fun cancelSpeechRecognition() {
        // Placeholder for iOS speech recognition
    }

    actual fun setRecognizing(recognizing: Boolean) {
        _uiState.update { it.copy(recognizing = recognizing) }
    }

    actual fun setRecognizedText(text: String) {
        _uiState.update { it.copy(recognizedText = text) }
    }
}