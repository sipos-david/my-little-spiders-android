package dev.shipi.mylittlespiders.components.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ErrorScreen(message: String? = null) {
    Column {
        LargeTopAppBar(
            title = {
                Text(
                    text = "Oh no!",
                    style = MaterialTheme.typography.headlineMedium
                )
            },
        )
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column {
                Icon(
                    modifier = Modifier
                        .height(128.dp)
                        .width(128.dp),
                    imageVector = Icons.Rounded.Warning,
                    contentDescription = "Warning symbol",
                    tint = MaterialTheme.colorScheme.error
                )
                Text(
                    text = "Something went wrong",
                    style = MaterialTheme.typography.headlineSmall
                )
                if (message != null) {
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ErrorScreenWithMessagePreview() {
    ErrorScreen("Reason")
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ErrorScreenNoMessagePreview() {
    ErrorScreen()
}