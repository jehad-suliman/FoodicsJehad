package com.jehad.foodics.data.repository

import com.jehad.foodics.data.local.dao.CategoryDao
import com.jehad.foodics.data.remote.api.ApiService
import com.jehad.foodics.domain.model.Category
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.jehad.foodics.data.local.entity.toDomain
import com.jehad.foodics.data.remote.model.toEntity


class CategoryRepository(
    private val categoryDao: CategoryDao,
    private val apiService: ApiService
) {
    fun getCategories(): Flow<List<Category>> {
        return categoryDao.getAllCategories().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun refreshCategories() {
        try {
            val categories = apiService.getCategories()
            categoryDao.insertCategories(categories.map { it.toEntity() })
        } catch (e: Exception) {
            // Handle error
        }
    }
}