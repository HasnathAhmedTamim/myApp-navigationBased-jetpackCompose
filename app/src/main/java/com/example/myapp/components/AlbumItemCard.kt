package com.example.myapp.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapp.model.AlbumItem

/**
 * Reusable Album Item Card
 *
 * Displays album item with title and description.
 * Used in AlbumScreen and AlbumDetailScreen.
 *
 * @param item The album item to display
 * @param onClick Callback when card is clicked
 * @param modifier Optional modifier for customization
 */
@Composable
fun AlbumItemCard(
    item: AlbumItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = item.description,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
