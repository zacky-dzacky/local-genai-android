@file:JvmName("SharedDi") // Or any other unique name

package com.example.local_genai_android.shared.di

import com.example.local_genai_android.shared.SharedViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.core.KoinApplication
import kotlin.jvm.JvmName

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
//@JvmName("initKoiniOS")
//fun initKoin() = initKoin {}

typealias KoinAppDeclaration = KoinApplication.() -> Unit
