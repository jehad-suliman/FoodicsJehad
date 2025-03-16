package com.jehad.foodics.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jehad.foodics.domain.model.Category

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey val id: String,
    val name: String
)

fun CategoryEntity.toDomain() = Category(
    id = id,
    name = name
)