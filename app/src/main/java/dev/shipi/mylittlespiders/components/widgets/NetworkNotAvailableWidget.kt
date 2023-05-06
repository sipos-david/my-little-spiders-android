package dev.shipi.mylittlespiders.components.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun NetworkNotAvailableWidget() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clip(MaterialTheme.shapes.extraSmall)
    ) {
        Text(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.errorContainer)
                .padding(vertical = 4.dp)
                .fillMaxWidth(),
            text = "Network not available!",
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onErrorContainer,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview
@Composable
fun NetworkNotAvailableWidgetPreview() {
    NetworkNotAvailableWidget()
}