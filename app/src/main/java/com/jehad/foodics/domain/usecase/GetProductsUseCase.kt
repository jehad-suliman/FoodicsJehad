package com.jehad.foodics.domain.usecase

import com.jehad.foodics.data.repository.ProductRepository
import com.jehad.foodics.domain.model.Product
import kotlinx.coroutines.flow.Flow

class GetProductsUseCase(private val productRepository: ProductRepository) {
    operator fun invoke(): Flow<List<Product>> {
        return productRepository.getAllProducts()
    }

    fun getProductsByCategory(categoryId: String): Flow<List<Product>> {
        return productRepository.getProductsByCategory(categoryId)
    }

    fun searchProducts(query: String): Flow<List<Product>> {
        return productRepository.searchProducts(query)
    }

    suspend fun refreshProducts() {
        productRepository.refreshProducts()
    }
}