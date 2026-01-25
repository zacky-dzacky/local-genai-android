package com.example.local_genai_android.shared

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import com.example.local_genai_android.shared.platform.AppContext
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private const val AUDIO_METER_MIN_DB = -2.0f
private const val AUDIO_METER_MAX_DB = 100.0f

actual class HoldToDictateViewModel actual constructor(
    private val appContext: AppContext
) : ViewModel(), RecognitionListener, KoinComponent {

    private val context: Context by inject()
    private val speechRecognizer: SpeechRecognizer
    private val recognizerIntent: Intent
    private var onRecognitionDone: ((String) -> Unit)? = null
    private var onAmplitudeChanged: ((Int) -> Unit)? = null

    private val _uiState = MutableStateFlow(HoldToDictateUiState())
    actual val uiState = _uiState.asStateFlow()

    init {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
            setRecognitionListener(this@HoldToDictateViewModel)
        }

        recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
        }
    }

    override fun onReadyForSpeech(params: Bundle?) {}

    override fun onBeginningOfSpeech() {}

    override fun onRmsChanged(rmsdB: Float) {
        onAmplitudeChanged?.invoke(convertRmsDbToAmplitude(rmsdB = rmsdB))
    }

    override fun onBufferReceived(buffer: ByteArray?) {}

    override fun onEndOfSpeech() {}

    override fun onError(error: Int) {}

    override fun onResults(results: Bundle?) {
        val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        if (matches != null && matches.size > 0) {
            setRecognizedText(matches[0] ?: "")
        } else {
            setRecognizedText("")
        }

        onRecognitionDone?.invoke(uiState.value.recognizedText)

        setRecognizing(recognizing = false)
    }

    override fun onPartialResults(partialResults: Bundle?) {
        val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        if (matches != null && matches.size > 0) {
            setRecognizedText(matches[0] ?: "")
        } else {
            setRecognizedText("")
        }
    }

    override fun onEvent(eventType: Int, params: Bundle?) {}

    actual fun startSpeechRecognition(onDone: (String) -> Unit, onAmplitudeChanged: (Int) -> Unit) {
        onRecognitionDone = onDone
        this.onAmplitudeChanged = onAmplitudeChanged

        speechRecognizer.startListening(recognizerIntent)
        setRecognizedText(text = "")
        setRecognizing(recognizing = true)
    }

    actual fun stopSpeechRecognition() {
        viewModelScope.launch {
            delay(500)
            speechRecognizer.stopListening()
            setRecognizing(recognizing = false)
        }
    }

    actual fun cancelSpeechRecognition() {
        setRecognizing(recognizing = false)
    }

    actual fun setRecognizing(recognizing: Boolean) {
        _uiState.update { it.copy(recognizing = recognizing) }
    }

    actual fun setRecognizedText(text: String) {
        _uiState.update { it.copy(recognizedText = text) }
    }
}

private fun convertRmsDbToAmplitude(rmsdB: Float): Int {
    val clampedRmsdB = rmsdB.coerceIn(AUDIO_METER_MIN_DB, AUDIO_METER_MAX_DB)
    return ((clampedRmsdB - AUDIO_METER_MIN_DB) * 65535f / (AUDIO_METER_MAX_DB - AUDIO_METER_MIN_DB)).toInt()
}
