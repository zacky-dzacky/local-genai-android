package com.example.local_genai_android.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.local_genai_android.shared.data.EMPTY_MODEL
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SharedUiState(
    val processing: Boolean = false,
    val generatedText: String = "",
    val functionCallDetails: List<String> = emptyList(),
    val noFunctionRecognized: Boolean = false,
    val error: String? = null,
    val criticalError: String? = null // For dialogs
)

class SharedViewModel(
    private val genAiService: GenAiService,
    private val stringFormatter: StringFormatter
) : ViewModel() {

    private val _uiState = MutableStateFlow(SharedUiState())
    val uiState = _uiState.asStateFlow()
    private val logger = Logger()

    fun processUserPrompt(userPrompt: String) {
        _uiState.update { SharedUiState(processing = true) } // Reset state

        viewModelScope.launch {
            // In a real app, the tools would be defined more dynamically.
            val tools = listOf(Tool(name = "someTool"))

            val model = EMPTY_MODEL
            genAiService.processUserPrompt(
                model = model,
                prompt = userPrompt,
                tools = tools,
                onResult = { actions ->
                    if (actions.isNotEmpty()) {
                        logger.d("SharedViewModel", "Actions count: ${actions.size}")
                        handleActions(actions)
                    } else {
                        logger.d("SharedViewModel", "No function recognized.")
                        _uiState.update { it.copy(processing = false, noFunctionRecognized = true) }
                    }
                },
                onError = { error ->
                    logger.e("SharedViewModel", "Critical error processing prompt: $error")
                    // Use criticalError for dialogs, as in the original app
                    _uiState.update { it.copy(processing = false, criticalError = error) }
                }
            )
        }
    }

    private fun handleActions(actions: List<Action>) {
        viewModelScope.launch {
            val actionResults = mutableListOf<String>()
            val functionDetails = mutableListOf<String>()
            val errors = mutableListOf<String>()

            for (action in actions) {
                try {
                    val result = genAiService.performAction(action)
                    actionResults.add(result)
                    functionDetails.add(stringFormatter.formatAction(action))
                } catch (e: Exception) {
                    val errorMessage = "Error performing action ${action.name}: ${e.message}"
                    logger.e("SharedViewModel", errorMessage, e)
                    errors.add(errorMessage)
                }
            }

            val combinedResult = actionResults.joinToString("\n")
            // Use regular error for snackbars
            val combinedErrors = if (errors.isNotEmpty()) errors.joinToString(";\n") else null

            _uiState.update {
                it.copy(
                    processing = false,
                    generatedText = combinedResult,
                    functionCallDetails = functionDetails,
                    error = combinedErrors
                )
            }
        }
    }

    fun onDialogDismissed() {
        _uiState.update { it.copy(criticalError = null) }
    }
}