package com.jehad.foodics.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jehad.foodics.domain.model.OrderItem
import com.jehad.foodics.domain.model.Product

@Entity(tableName = "order_items")
data class OrderItemEntity(
    @PrimaryKey val productId: String,
    val productName: String,
    val productPrice: Double,
    val quantity: Int
)

fun OrderItemEntity.toDomain(): OrderItem {
    return OrderItem(
        product = Product(
            id = this.productId,
            name = this.productName,
            price = this.productPrice
        ),
        quantity = this.quantity
    )
}