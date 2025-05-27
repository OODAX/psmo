package com.example.mvvmdbapp

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MvvmDbApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MvvmDbApp)
            modules(appModule)
        }
    }
}

val appModule = module {
    single { AppDatabase.getDatabase(get()) }
    single { get<AppDatabase>().userDao() }
    single { UserRepository(get()) }
    single { UserViewModel(get(),get()) }
}
