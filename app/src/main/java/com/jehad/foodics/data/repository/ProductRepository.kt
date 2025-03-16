package com.jehad.foodics.data.repository

import com.jehad.foodics.data.local.dao.ProductDao
import com.jehad.foodics.data.remote.api.ApiService
import com.jehad.foodics.domain.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.jehad.foodics.data.local.entity.toDomain
import com.jehad.foodics.data.remote.model.toEntity

class ProductRepository(
    private val productDao: ProductDao,
    private val apiService: ApiService
) {
    fun getAllProducts(): Flow<List<Product>> {
        return productDao.getAllProducts().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    fun getProductsByCategory(categoryId: String): Flow<List<Product>> {
        return productDao.getProductsByCategory(categoryId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    fun searchProducts(query: String): Flow<List<Product>> {
        return productDao.searchProducts(query).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun refreshProducts() {
        try {
            val products = apiService.getProducts()
            productDao.insertProducts(products.map { it.toEntity() })
        } catch (e: Exception) {
            // Handle error
        }
    }
}