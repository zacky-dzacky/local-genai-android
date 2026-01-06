package com.example.local_genai_android.shared.di

import com.example.local_genai_android.shared.SharedViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.core.KoinApplication

val sharedViewModelModule = module {
    single { SharedViewModel(get(), get()) }
}

expect fun platformModule(): Module

fun initKoin(appDeclaration: KoinAppDeclaration = {}) = 
    startKoin {
        appDeclaration()
        modules(sharedViewModelModule, platformModule())
    }

// For iOS
fun initKoin() = initKoin {}

typealias KoinAppDeclaration = KoinApplication.() -> Unit
