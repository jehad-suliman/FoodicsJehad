package com.jehad.foodics.ui.screens.tables

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jehad.foodics.data.repository.OrderRepository
import com.jehad.foodics.domain.model.Category
import com.jehad.foodics.domain.model.Product
import com.jehad.foodics.domain.usecase.GetCategoriesUseCase
import com.jehad.foodics.domain.usecase.GetProductsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TablesViewModel(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getProductsUseCase: GetProductsUseCase,
    private val orderRepository: OrderRepository
) : ViewModel() {
    private val _state = MutableStateFlow(TablesState())
    val state: StateFlow<TablesState> = _state.asStateFlow()


    init {
        viewModelScope.launch {
            // Load initial data
            getCategoriesUseCase.refreshCategories()
            getProductsUseCase.refreshProducts()

            // Collect categories
            getCategoriesUseCase().collect { categories ->
                _state.update { it.copy(categories = categories) }

                // Select first category if available and not already selected
                if (categories.isNotEmpty() && _state.value.selectedCategoryId == null) {
                    selectCategory(categories.first().id)
                }
            }
        }

        viewModelScope.launch {
            orderRepository.getTotalItems().collect { itemCount ->
                _state.update { it.copy(orderItemCount = itemCount) }
            }
        }

        viewModelScope.launch {
            orderRepository.getTotalPrice().collect { total ->
                _state.update { it.copy(totalPrice = total) }
            }
        }

    }

    fun selectCategory(categoryId: String) {
        if (_state.value.selectedCategoryId != categoryId) {
            _state.update { it.copy(selectedCategoryId = categoryId) }
            getProductsByCategoryId(categoryId)
        }
    }

    private fun getProductsByCategoryId(categoryId: String) {
        viewModelScope.launch {
            getProductsUseCase.getProductsByCategory(categoryId).collect { products ->
                _state.update { it.copy(products = products) }
            }
        }
    }

    fun addProductToOrder(product: Product) {
        viewModelScope.launch {
            orderRepository.addOrUpdateOrderItem(product)
        }
    }
}

data class TablesState(
    val categories: List<Category> = emptyList(),
    val products: List<Product> = emptyList(),
    val selectedCategoryId: String? = null,
    val orderItemCount: Int = 0,
    val totalPrice: Double = 0.0,
    val isLoading: Boolean = false,
    val error: String? = null
)