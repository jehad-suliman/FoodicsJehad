package com.jehad.foodics.ui.screens.tables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jehad.foodics.ui.components.BottomNavItem
import com.jehad.foodics.ui.components.CategoryTabs
import com.jehad.foodics.ui.components.OrderButton
import com.jehad.foodics.ui.components.ProductGrid
import com.jehad.foodics.ui.components.SearchBar
import com.jehad.foodics.ui.navigation.LocalNavController
import org.koin.androidx.compose.koinViewModel

@Composable
fun MenuScreen(
    viewModel: MenuViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val navController = LocalNavController.current

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SearchBar(
            query = "",
            onQueryChange = {},
            modifier = Modifier.padding(8.dp)
        )


        if (state.categories.isNotEmpty()) {
            CategoryTabs(
                categories = state.categories,
                selectedCategoryId = state.selectedCategoryId ?: state.categories.first().id,
                onCategoryClick = { category ->
                    viewModel.selectCategory(category.id)
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                )
            } else if (state.products.isEmpty()) {
                Text(
                    text = "No products found",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .align(Alignment.Center)
                )
            } else {

                ProductGrid(
                    products = state.products,
                    onProductClick = { product ->
                        viewModel.addProductToOrder(product)
                    }
                )
            }

            if (state.error != null) {
                Text(
                    text = state.error!!,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .align(Alignment.Center)
                )
            }
        }
        val orderItemCount = state.orderItemCount
        if (orderItemCount > 0) {
            OrderButton(state.orderItemCount, price = state.totalPrice.toString()) {
                navController.navigate(BottomNavItem.Orders.route)
            }
        }

    }
}