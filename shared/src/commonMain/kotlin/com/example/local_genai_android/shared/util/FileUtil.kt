package com.example.local_genai_android.shared.util

expect fun getModelPath(
    filename: String,
    imported: Boolean,
    localModelFilePathOverride: String,
    localFileRelativeDirPathOverride: String,
    normalizedName: String,
    version: String,
    isZip: Boolean,
    unzipDir: String
): String