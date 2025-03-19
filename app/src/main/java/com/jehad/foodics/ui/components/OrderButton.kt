package com.jehad.foodics.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun OrderButton(
    orderNumber: Int, orderText: String = "View Order", price: String,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick.invoke() }
            .padding(16.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF6A1B9A)) // Purple background color
            .padding(16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.White, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = orderNumber.toString(),
                    color = Color(0xFF6A1B9A),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = orderText,
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = "$$price",
                modifier = Modifier.padding(horizontal = 15.dp),
                color = Color.White,
                style = MaterialTheme.typography.titleLarge,
            )

            Icon(
                imageVector = Icons.Default.ArrowForward,
                modifier = Modifier.size(35.dp),
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OrderButtonPreview() {
    MaterialTheme {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OrderButton(
                orderNumber = 1,
                price = "24.99",
                onClick = {}
            )

            OrderButton(
                orderNumber = 2,
                orderText = "Track Order",
                price = "49.50",
                onClick = {}
            )

            OrderButton(
                orderNumber = 3,
                orderText = "Order Details",
                price = "199.99",
                onClick = {}
            )
        }
    }
}