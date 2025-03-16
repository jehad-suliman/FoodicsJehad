package com.jehad.foodics.ui.components


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.jehad.foodics.domain.model.Category

@Composable
fun CategoryItem(
    category: Category,
    isSelected: Boolean,
    onCategoryClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    val textColor = if (isSelected) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .background(backgroundColor)
            .clickable { onCategoryClick(category.id) }
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = category.name,
            style = MaterialTheme.typography.bodyMedium,
            color = textColor
        )
    }
}