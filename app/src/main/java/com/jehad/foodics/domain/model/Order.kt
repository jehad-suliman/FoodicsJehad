package com.jehad.foodics.domain.model


data class Order(
    val items: MutableMap<String, OrderItem> = mutableMapOf()
) {
    fun addItem(product: Product) {
        val existingItem = items[product.id]
        if (existingItem != null) {
            items[product.id] = existingItem.copy(quantity = existingItem.quantity + 1)
        } else {
            items[product.id] = OrderItem(product, 1)
        }
    }

    fun clear() {
        items.clear()
    }

    fun getTotalPrice(): Double {
        return items.values.sumOf { it.product.price * it.quantity }
    }

    fun getTotalItems(): Int {
        return items.values.sumOf { it.quantity }
    }
}

data class OrderItem(
    val product: Product,
    val quantity: Int
)