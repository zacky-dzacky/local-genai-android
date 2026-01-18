package com.example.local_genai_android.ui.textandvoiceinput

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.local_genai_android.shared.SharedUiState
import com.example.local_genai_android.shared.SharedViewModel
import com.example.local_genai_android.shared.StringFormatter
import com.example.local_genai_android.shared.di.sharedViewModelModule
import org.koin.compose.koinInject

//import com.example.local_genai_android.ui.theme.HoldToDictateViewModel
//import org.koin.androidx.compose.koinViewModel
//import org.koin.compose.koinInject

@Composable
fun PromptScreen(
    snackbarHostState: SnackbarHostState,
//    sharedViewModel: SharedViewModel = koinViewModel(),
//    holdToDictateViewModel: HoldToDictateViewModel = koinViewModel(),
    stringFormatter: StringFormatter = koinInject()
) {
    val uiState by lazy {
       SharedUiState(
           processing = false,
           generatedText = "",
           functionCallDetails = emptyList(),
           noFunctionRecognized = false,
       )
    }
    //sharedViewModel.uiState.collectAsState()
    var clearInputTextTrigger by remember { mutableStateOf(0L) }
    var curAmplitude by remember { mutableStateOf(0) }

    // Show snackbar for non-critical errors or unrecognized commands
    LaunchedEffect(uiState.error, uiState.noFunctionRecognized) {
        if (uiState.error != null) {
            snackbarHostState.showSnackbar(
                message = "Error: ${uiState.error}",
                withDismissAction = true,
                duration = SnackbarDuration.Long
            )
        }
        if (uiState.noFunctionRecognized) {
            snackbarHostState.showSnackbar(
                message = stringFormatter.getNoFunctionCallMessage(),
                withDismissAction = true,
                duration = SnackbarDuration.Long
            )
        }
    }

    // Show dialog for critical errors
    if (uiState.criticalError != null) {
        AlertDialog(
            onDismissRequest = {
//                sharedViewModel.onDialogDismissed()
           },
            title = { Text("Error") },
            text = { Text(uiState.criticalError!!) },
            confirmButton = {
                TextButton(onClick = {
//                    sharedViewModel.onDialogDismissed()
                }) {
                    Text("OK")
                }
            }
        )
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        LazyColumn(modifier = Modifier.weight(1f).padding(16.dp)) {
            if (uiState.generatedText.isNotEmpty()) {
                item {
                    Text(text = uiState.generatedText)
                }
            }
            items(uiState.functionCallDetails) { detail ->
                Text(text = detail, modifier = Modifier.padding(top = 4.dp))
            }
        }

        if (uiState.processing) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp).align(Alignment.CenterHorizontally))
        }

        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            TextAndVoiceInput(
                processing = uiState.processing,
//                holdToDictateViewModel = , //holdToDictateViewModel,
                onDone = { text ->
//                    sharedViewModel.processUserPrompt(text)
                    clearInputTextTrigger = System.currentTimeMillis()
                },
                onAmplitudeChanged = { curAmplitude = it },
                clearTextTrigger = clearInputTextTrigger,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
