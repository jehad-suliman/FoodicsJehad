package com.jehad.foodics.ui.screens.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jehad.foodics.data.repository.OrderRepository
import com.jehad.foodics.domain.model.Category
import com.jehad.foodics.domain.model.OrderItem
import com.jehad.foodics.domain.model.Product
import com.jehad.foodics.domain.usecase.GetCategoriesUseCase
import com.jehad.foodics.domain.usecase.GetProductsUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MenuViewModel(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getProductsUseCase: GetProductsUseCase,
    private val orderRepository: OrderRepository
) : ViewModel() {
    private val _state = MutableStateFlow(TablesState())
    val state: StateFlow<TablesState> = _state.asStateFlow()

    private val searchDebounceTimeoutMs = 300L
    private var searchJob: Job? = null

    init {
        viewModelScope.launch {
            getCategoriesUseCase.refreshCategories()
            getProductsUseCase.refreshProducts()

            getCategoriesUseCase().collect { categories ->
                _state.update { it.copy(categories = categories, isLoading = false) }

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

        viewModelScope.launch {
            orderRepository.getOrderItems().collect { orderItems ->
                updateProductQuantities(orderItems)
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
            _state.update { it.copy(isLoading = true) }
            val requestedCategoryId = categoryId

            getProductsUseCase.getProductsByCategory(categoryId).combine(
                orderRepository.getOrderItems()
            ) { products, orderItems ->
                products.map { product ->
                    val orderItem = orderItems.find { it.product.id == product.id }
                    product.copy(quantity = orderItem?.quantity ?: 0)
                }
            }.collect { productsWithQuantity ->
                if (_state.value.selectedCategoryId == requestedCategoryId) {
                    _state.update {
                        it.copy(
                            products = productsWithQuantity,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun addProductToOrder(product: Product) {
        viewModelScope.launch {
            val currentCategoryId = _state.value.selectedCategoryId
            val isSearching = _state.value.isSearching

            orderRepository.addOrUpdateOrderItem(product)
            if (isSearching && _state.value.searchQuery.isNotEmpty()) {
                searchProducts(_state.value.searchQuery)
            } else if (currentCategoryId != null && currentCategoryId != _state.value.selectedCategoryId) {
                selectCategory(currentCategoryId)
            }
        }
    }

    private fun updateProductQuantities(orderItems: List<OrderItem>) {
        val currentProducts = _state.value.products

        val updatedProducts = currentProducts.map { product ->
            val orderItem = orderItems.find { it.product.id == product.id }
            val newQuantity = orderItem?.quantity ?: 0

            if (product.quantity != newQuantity) {
                product.copy(quantity = newQuantity)
            } else {
                product
            }
        }

        _state.update { it.copy(products = updatedProducts) }
    }


    fun updateSearchQuery(query: String) {
        _state.update { it.copy(searchQuery = query) }

        searchJob?.cancel()

        if (query.isBlank()) {
            _state.value.selectedCategoryId?.let { categoryId ->
                getProductsByCategoryId(categoryId)
            }
            _state.update { it.copy(isSearching = false) }
        } else {
            _state.update { it.copy(isSearching = true) }
            searchJob = viewModelScope.launch {
                delay(searchDebounceTimeoutMs)
                searchProducts(query)
            }
        }
    }

    private fun searchProducts(query: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            try {
                getProductsUseCase.getAllProducts().combine(
                    orderRepository.getOrderItems()
                ) { products, orderItems ->
                    products
                        .filter { product ->
                            product.name.contains(query, ignoreCase = true) ||
                                    product.description?.contains(query, ignoreCase = true) == true
                        }
                        .map { product ->
                            val orderItem = orderItems.find { it.product.id == product.id }
                            product.copy(quantity = orderItem?.quantity ?: 0)
                        }
                }.collect { filteredProducts ->
                    _state.update {
                        it.copy(
                            products = filteredProducts,
                            isLoading = false,
                            error = if (filteredProducts.isEmpty()) "No products found" else null
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        error = "Error searching products: ${e.message}",
                        isLoading = false
                    )
                }
            }
        }
    }

}

data class TablesState(
    val categories: List<Category> = emptyList(),
    val products: List<Product> = emptyList(),
    val selectedCategoryId: String? = null,
    val orderItemCount: Int = 0,
    val totalPrice: Double = 0.0,
    val isLoading: Boolean = true,
    val error: String? = null,
    val searchQuery: String = "",
    val isSearching: Boolean = false
)