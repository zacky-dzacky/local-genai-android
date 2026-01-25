package com.example.local_genai_android.ui.textandvoiceinput

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import com.example.local_genai_android.R
import com.example.local_genai_android.shared.HoldToDictateViewModel
import kotlin.coroutines.cancellation.CancellationException

@Deprecated("Use HoldToDictateViewModel instead")
@Composable
fun HoldToDictate(
    viewModel: HoldToDictateViewModel,
    onDone: (String) -> Unit,
    onAmplitudeChanged: (Int) -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    var recordAudioPermissionGranted by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val recordAudioPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
                permissionGranted ->
            if (permissionGranted) {
                recordAudioPermissionGranted = true
            }
        }

    LaunchedEffect(Unit) {
        // Check permission
        when (PackageManager.PERMISSION_GRANTED) {
            // Already got permission.
            ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) -> {
                recordAudioPermissionGranted = true
            }

            // Otherwise, ask for permission
            else -> {
                recordAudioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
        }
    }

    if (recordAudioPermissionGranted) {
        Box(
            modifier =
                modifier.then(
                    if (enabled) {
                        Modifier.pointerInput(Unit) {
                            detectTapGestures(
                                onPress = {
                                    viewModel.startSpeechRecognition(
                                        onDone = onDone,
                                        onAmplitudeChanged = onAmplitudeChanged,
                                    )
                                    try {
                                        awaitRelease()
                                    } catch (e: CancellationException) {
                                        // Move out of the button to cancel it.
                                        viewModel.cancelSpeechRecognition()
                                        return@detectTapGestures
                                    }

                                    // Release to stop recognition.
                                    viewModel.stopSpeechRecognition()
                                }
                            )
                        }
                    } else {
                        Modifier
                    }
                )
                .clip(CircleShape)
                .graphicsLayer { alpha = if (enabled) 1f else 0.5f }
                .background(Color.Red), // Using a default color now
            contentAlignment = Alignment.Center,
        ) {
            Text(
                stringResource(if (uiState.recognizing) R.string.listening else R.string.hold_down_to_talk),
                color = Color.White,
            )
        }
    }
}