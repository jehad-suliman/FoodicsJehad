package com.jehad.foodics.ui.screens.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jehad.foodics.data.repository.OrderRepository
import com.jehad.foodics.domain.model.OrderItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class OrderViewModel(private val orderRepository: OrderRepository) : ViewModel() {
    private val _state = MutableStateFlow(OrderState())
    val state: StateFlow<OrderState> = _state.asStateFlow()

    init {
        observeOrder()
    }

    private fun observeOrder() {
        viewModelScope.launch {
            combine(
                orderRepository.getOrderItems(),
                orderRepository.getTotalPrice(),
                orderRepository.getTotalItems()
            ) { items, totalPrice, totalItems ->
                OrderState(items = items, totalPrice = totalPrice, totalItems = totalItems)
            }.collect { newState ->
                _state.value = newState
            }
        }
    }

    fun clearOrder() {
        viewModelScope.launch {
            orderRepository.clearOrder()
        }
    }
}

data class OrderState(
    val items: List<OrderItem> = emptyList(),
    val totalPrice: Double = 0.0,
    val totalItems: Int = 0
)