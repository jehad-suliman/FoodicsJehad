package com.jehad.foodics.data.remote.model

import com.jehad.foodics.data.local.entity.ProductEntity
import kotlinx.serialization.Serializable

@Serializable
data class ProductResponse(
    val id: String,
    val name: String,
    val description: String? = null,
    val image: String? = null,
    val price: Double,
    val category: CategoryResponse
)

fun ProductResponse.toEntity() = ProductEntity(
    id = id,
    name = name,
    description = description,
    image = image,
    price = price,
    category = ProductEntity.CategoryData(
        id = category.id,
        name = category.name
    )
)