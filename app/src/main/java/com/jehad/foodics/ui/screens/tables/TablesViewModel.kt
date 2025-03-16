package com.jehad.foodics.ui.screens.tables

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jehad.foodics.domain.model.Category
import com.jehad.foodics.domain.model.Order
import com.jehad.foodics.domain.model.Product
import com.jehad.foodics.domain.usecase.GetCategoriesUseCase
import com.jehad.foodics.domain.usecase.GetProductsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TablesViewModel(
    private val getCategoriesUseCase:   GetCategoriesUseCase,
    private val getProductsUseCase: GetProductsUseCase,
    private val order: Order
) : ViewModel() {
    private val _state = MutableStateFlow(TablesState())
    val state: StateFlow<TablesState> = _state.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

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

        // Handle search
        viewModelScope.launch {
            _searchQuery
                .debounce(300)
                .collect { query ->
                    if (query.isBlank()) {
                        // If search is empty, show products by selected category
                        _state.value.selectedCategoryId?.let {
                            getProductsByCategoryId(it)
                        }
                    } else {
                        // Search products
                        getProductsUseCase.searchProducts(query).collect { products ->
                            _state.update { it.copy(products = products) }
                        }
                    }
                }
        }
    }

    fun selectCategory(categoryId: String) {
        if (_state.value.selectedCategoryId != categoryId) {
            _state.update { it.copy(selectedCategoryId = categoryId) }
            getProductsByCategoryId(categoryId)
            // Clear search when changing category
            _searchQuery.value = ""
        }
    }

    private fun getProductsByCategoryId(categoryId: String) {
        viewModelScope.launch {
            getProductsUseCase.getProductsByCategory(categoryId).collect { products ->
                _state.update { it.copy(products = products) }
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun addProductToOrder(product: Product) {
        order.addItem(product)
        _state.update { it.copy(orderItemCount = order.getTotalItems()) }
    }

    fun clearOrder() {
        order.clear()
        _state.update { it.copy(orderItemCount = 0) }
    }
}

data class TablesState(
    val categories: List<Category> = emptyList(),
    val products: List<Product> = emptyList(),
    val selectedCategoryId: String? = null,
    val orderItemCount: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null
)