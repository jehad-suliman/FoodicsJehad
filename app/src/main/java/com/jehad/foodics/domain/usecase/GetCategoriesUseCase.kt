package com.jehad.foodics.domain.usecase

import com.jehad.foodics.data.repository.CategoryRepository
import com.jehad.foodics.domain.model.Category
import kotlinx.coroutines.flow.Flow

class GetCategoriesUseCase(private val categoryRepository: CategoryRepository) {
    operator fun invoke(): Flow<List<Category>> {
        return categoryRepository.getCategories()
    }

    suspend fun refreshCategories() {
        categoryRepository.refreshCategories()
    }
}