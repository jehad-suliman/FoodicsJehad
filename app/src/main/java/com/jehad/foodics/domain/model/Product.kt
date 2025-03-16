package com.jehad.foodics.domain.model

data class Product(
    val id: String,
    val name: String,
    val description: String,
    val imageUrl: String,
    val price: Double,
    val category: Category
)