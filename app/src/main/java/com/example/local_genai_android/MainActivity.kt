package com.example.local_genai_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.local_genai_android.shared.HoldToDictateViewModel
import com.example.local_genai_android.shared.viewmodel.SharedViewModel
import com.example.local_genai_android.ui.textandvoiceinput.PromptScreen
import com.example.local_genai_android.ui.theme.LocalgenaiandroidTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val sharedViewModel: SharedViewModel by inject()
            val holdToDictateViewModel: HoldToDictateViewModel by inject()
            LocalgenaiandroidTheme {
                val snackbarHostState = remember { SnackbarHostState() }
                Scaffold(
                    snackbarHost = { SnackbarHost(snackbarHostState) }
                ) { padding ->
                    Surface(
                        modifier = Modifier.fillMaxSize().padding(padding),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        Text(text = "asdfasdf")
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