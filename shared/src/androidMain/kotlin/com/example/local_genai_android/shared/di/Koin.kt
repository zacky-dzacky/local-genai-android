package com.example.local_genai_android.shared.di

import com.example.local_genai_android.shared.GenAiService
import com.example.local_genai_android.shared.StringFormatter
import com.example.local_genai_android.shared.platform.AppContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module = module {
    single { GenAiService(get()) }
    single { StringFormatter(get()) }
    single { AppContext() }
}