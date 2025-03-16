package com.jehad.foodics.ui.screens.orders

import androidx.lifecycle.ViewModel
import com.jehad.foodics.domain.model.Order
import com.jehad.foodics.domain.model.OrderItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class OrderViewModel(private val order: Order) : ViewModel() {
    private val _state = MutableStateFlow(OrderState())
    val state: StateFlow<OrderState> = _state.asStateFlow()

    init {
        updateOrderState()
    }

    private fun updateOrderState() {
        _state.update {
            OrderState(
                items = order.items.values.toList(),
                totalPrice = order.getTotalPrice(),
                totalItems = order.getTotalItems()
            )
        }
    }

    fun clearOrder() {
        order.clear()
        updateOrderState()
    }
}

data class OrderState(
    val items: List<OrderItem> = emptyList(),
    val totalPrice: Double = 0.0,
    val totalItems: Int = 0
)