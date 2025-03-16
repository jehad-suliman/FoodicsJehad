package com.jehad.foodics.data.remote.model

import com.jehad.foodics.data.local.entity.CategoryEntity
import kotlinx.serialization.Serializable

@Serializable
data class CategoryResponse(
    val id: String,
    val name: String
)

fun CategoryResponse.toEntity() = CategoryEntity(
    id = id,
    name = name
)