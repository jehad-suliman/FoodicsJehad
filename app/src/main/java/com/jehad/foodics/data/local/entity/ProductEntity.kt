package com.jehad.foodics.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jehad.foodics.domain.model.Category
import com.jehad.foodics.domain.model.Product

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String?,
    val image: String?,
    val price: Double,
    @Embedded(prefix = "category_") val category: CategoryData
) {
    data class CategoryData(
        val id: String,
        val name: String
    )
}

fun ProductEntity.toDomain() = Product(
    id = id,
    name = name,
    description = description ?: "",
    imageUrl = image ?: "",
    price = price,
    category = Category(
        id = category.id,
        name = category.name
    )
)