package com.jehad.foodics.domain.model

data class Product(
    val id: String,
    val name: String,
    val description: String? = null,
    val imageUrl: String? = null,
    val price: Double? = null,
    val category: Category? = null,
    val quantity: Int = 0
)