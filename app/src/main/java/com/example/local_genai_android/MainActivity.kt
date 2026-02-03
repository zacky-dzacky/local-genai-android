package com.example.local_genai_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.local_genai_android.shared.HoldToDictateViewModel
import com.example.local_genai_android.shared.viewmodel.SharedViewModel
import com.example.local_genai_android.ui.textandvoiceinput.PromptScreen
import com.example.local_genai_android.ui.theme.LocalgenaiandroidTheme
import org.koin.android.ext.android.inject
import androidx.compose.material3.SnackbarHost

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedViewModel: SharedViewModel by inject()
        val holdToDictateViewModel: HoldToDictateViewModel by inject()

        setContent {
            LocalgenaiandroidTheme {
                val snackbarHostState = remember { SnackbarHostState() }
                val coroutineScope = rememberCoroutineScope()

                val filePickerLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.GetContent(),
                    onResult = { uri ->
                        // Handle the selected file URI
                    }
                )

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Local GenAI") },
                            actions = {
                                Button(onClick = { filePickerLauncher.launch("*/*") }) {
                                    Text("New Button")
                                }
                            }
                        )
                    },
                    snackbarHost = { SnackbarHost(snackbarHostState) }
                ) { padding ->
                    Surface(
                        modifier = Modifier.fillMaxSize().padding(padding),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        PromptScreen(
                            holdToDictateViewModel = holdToDictateViewModel,
                            snackbarHostState = snackbarHostState,
                            sharedViewModel = sharedViewModel
                        )
                    }
                }
            }
        }
    }
}