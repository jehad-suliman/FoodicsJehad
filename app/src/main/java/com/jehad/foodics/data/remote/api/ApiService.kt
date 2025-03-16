package com.jehad.foodics.data.remote.api

import android.util.Log
import com.jehad.foodics.data.remote.model.CategoryResponse
import com.jehad.foodics.data.remote.model.ProductResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class ApiService(private val client: HttpClient) {

    private val categoriesUrl = "https://my.api.mockaroo.com/categories.json?key=53d8a070"
    private val productsUrl = "https://my.api.mockaroo.com/products.json?key=53d8a070"

    suspend fun getCategories(): List<CategoryResponse> {
        return try {
            client.get(categoriesUrl).body()
        } catch (e: Exception) {
            Log.e("ApiService", "categoriesUrl Error fetching categories${e.message}", e)
            emptyList()
        }
    }

    suspend fun getProducts(): List<ProductResponse> {
        return try {
            client.get(productsUrl).body()
        } catch (e: Exception) {
            Log.e("ApiService", "productsUrl Error fetching categories${e.message}", e)
            emptyList()
        }

    }
}