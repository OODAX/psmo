// AppModule.kt
package com.example.networkapp.di

import com.example.networkapp.MainViewModel
import com.example.networkapp.network.ApiService
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {

    single {
        Retrofit.Builder()
            .baseUrl("http://localhost/") // Will override dynamically
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single { get<Retrofit>().create(ApiService::class.java) }

    viewModel { MainViewModel(get()) }
}
