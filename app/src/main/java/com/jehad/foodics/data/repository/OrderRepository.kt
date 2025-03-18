package com.jehad.foodics.data.repository

import com.jehad.foodics.data.local.dao.OrderDao
import com.jehad.foodics.data.local.entity.OrderItemEntity
import com.jehad.foodics.data.local.entity.toDomain
import com.jehad.foodics.domain.model.OrderItem
import com.jehad.foodics.domain.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OrderRepository(private val orderDao: OrderDao) {

    fun getOrderItems(): Flow<List<OrderItem>> {
        return orderDao.getOrderItemsFlow().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun addOrUpdateOrderItem(product: Product) {
        val existingItem = orderDao.getOrderItemById(product.id)
        val updatedItem = existingItem?.copy(quantity = existingItem.quantity + 1)
            ?: OrderItemEntity(
                productId = product.id,
                productName = product.name,
                productPrice = product.price ?: 0.0,
                quantity = 1
            )

        orderDao.insertOrderItem(updatedItem)
    }

    suspend fun clearOrder() {
        orderDao.clearOrder()
    }

    fun getTotalPrice(): Flow<Double> {
        return orderDao.getTotalPriceFlow().map { it ?: 0.0 }
    }

    fun getTotalItems(): Flow<Int> {
        return orderDao.getTotalItemsFlow().map { it ?: 0 }
    }
}
