package com.jehad.foodics.di

import com.jehad.foodics.data.local.database.AppDatabase
import com.jehad.foodics.data.remote.api.ApiService
import com.jehad.foodics.data.repository.CategoryRepository
import com.jehad.foodics.data.repository.OrderRepository
import com.jehad.foodics.data.repository.ProductRepository
import com.jehad.foodics.domain.usecase.GetCategoriesUseCase
import com.jehad.foodics.domain.usecase.GetProductsUseCase
import com.jehad.foodics.ui.screens.orders.OrderViewModel
import com.jehad.foodics.ui.screens.tables.MenuViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // HttpClient
    single {
        HttpClient(Android) {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
            install(Logging) {
                level = LogLevel.ALL
            }
        }
    }

    // Database
    single { AppDatabase.getDatabase(androidContext()) }
    single { get<AppDatabase>().categoryDao() }
    single { get<AppDatabase>().productDao() }
    single { get<AppDatabase>().orderDao() }
    // API Service
    single { ApiService(get()) }

    // Repositories
    single { CategoryRepository(get(), get()) }
    single { ProductRepository(get(), get()) }
    single { OrderRepository(get()) }

    // Use Cases
    single { GetCategoriesUseCase(get()) }
    single { GetProductsUseCase(get()) }

    // ViewModels
    viewModel { MenuViewModel(get(), get(), get()) }
    viewModel { OrderViewModel(get()) }
}