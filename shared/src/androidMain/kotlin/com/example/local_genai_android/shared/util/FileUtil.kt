package com.example.local_genai_android.shared.util

import android.content.Context
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File


class Provider: KoinComponent {
    val context: Context by inject()
}

actual fun getModelPath(
    filename: String,
    imported: Boolean,
    localModelFilePathOverride: String,
    localFileRelativeDirPathOverride: String,
    normalizedName: String,
    version: String,
    isZip: Boolean,
    unzipDir: String): String {
    val context: Context = Provider().context

    if (imported) {
        return listOf(context.getExternalFilesDir(null)?.absolutePath ?: "", filename)
            .joinToString(File.separator)
    }

    if (localModelFilePathOverride.isNotEmpty()) {
        return localModelFilePathOverride
    }

    if (localFileRelativeDirPathOverride.isNotEmpty()) {
        return listOf(
            context.getExternalFilesDir(null)?.absolutePath ?: "",
            localFileRelativeDirPathOverride,
            filename,
        )
            .joinToString(File.separator)
    }

    val baseDir =
        listOf(context.getExternalFilesDir(null)?.absolutePath ?: "", normalizedName, version)
            .joinToString(File.separator)
    return if (isZip && unzipDir.isNotEmpty()) {
        listOf(baseDir, unzipDir).joinToString(File.separator)
    } else {
        listOf(baseDir, filename).joinToString(File.separator)
    }

    return ""
}