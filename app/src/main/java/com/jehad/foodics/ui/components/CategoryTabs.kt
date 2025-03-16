package com.jehad.foodics.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jehad.foodics.domain.model.Category

@Composable
fun CategoryTabs(
    categories: List<Category>,
    selectedCategoryId: String,
    onCategoryClick: (Category) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedIndex = categories.indexOfFirst { it.id == selectedCategoryId }

    TabRow(
        selectedTabIndex = selectedIndex,
        modifier = modifier.fillMaxWidth(),
        divider = {}, // Optional: Customize or remove the divider
        indicator = { tabPositions ->
            // Custom indicator: A colored underline for the selected tab
            Box(
                modifier = Modifier
                    .tabIndicatorOffsett(tabPositions[selectedIndex])
                    .height(2.dp) // Height of the indicator
                    .background(Color.Blue) // Color of the indicator
            )
        }
    ) {
        categories.forEachIndexed { index, category ->
            Tab(
                selected = selectedIndex == index,
                onClick = { onCategoryClick(category) },
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = category.name,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}

private fun Modifier.tabIndicatorOffsett(
    currentTabPosition: TabPosition
): Modifier = composed {
    val currentTabWidth = currentTabPosition.width
    val indicatorOffset = currentTabPosition.left
    fillMaxWidth()
        .wrapContentSize(Alignment.BottomStart)
        .offset(x = indicatorOffset)
        .width(currentTabWidth)
}