package com.jehad.foodics.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jehad.foodics.data.local.entity.OrderItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {

    @Query("SELECT * FROM order_items")
    fun getOrderItemsFlow(): Flow<List<OrderItemEntity>>

    @Query("SELECT * FROM order_items WHERE productId = :productId LIMIT 1")
    suspend fun getOrderItemById(productId: String): OrderItemEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderItem(item: OrderItemEntity)

    @Query("DELETE FROM order_items")
    suspend fun clearOrder()

    @Query("SELECT SUM(productPrice * quantity) FROM order_items")
    fun getTotalPriceFlow(): Flow<Double?>

    @Query("SELECT SUM(quantity) FROM order_items")
    fun getTotalItemsFlow(): Flow<Int?>
}