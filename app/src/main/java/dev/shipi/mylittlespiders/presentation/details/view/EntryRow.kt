package dev.shipi.mylittlespiders.presentation.details.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.shipi.mylittlespiders.domain.model.Entry
import java.time.LocalDate

@Composable
fun EntryRow(entry: Entry, onDeleteEntry: (Long) -> Unit, onNavigateToEditEntry: (Long) -> Unit) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp)
        )
        {
            Column(modifier = Modifier.fillMaxWidth(0.6f)) {
                Row(modifier = Modifier.padding(bottom = 4.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            modifier = Modifier.padding(end = 4.dp),
                            imageVector = Icons.Outlined.DateRange,
                            contentDescription = "Date icon"
                        )
                        Text(text = entry.date.toString())
                    }
                    Spacer(Modifier.padding(horizontal = 8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            modifier = Modifier.padding(end = 4.dp),
                            imageVector = Icons.Outlined.Favorite,
                            contentDescription = "Favorite icon"
                        )
                        Text(text = entry.respect.toString())
                    }
                }
                Text(text = entry.text, style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(Modifier.weight(1f))
            Button(
                modifier = Modifier
                    .width(48.dp)
                    .height(48.dp),
                contentPadding = PaddingValues(0.dp),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.secondary
                ),
                onClick = { onNavigateToEditEntry(entry.id) }) {
                Icon(imageVector = Icons.Outlined.Edit, contentDescription = "Edit entry")
            }
            Spacer(modifier = Modifier.padding(horizontal = 4.dp))
            Button(
                modifier = Modifier
                    .width(48.dp)
                    .height(48.dp),
                contentPadding = PaddingValues(0.dp),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.error
                ),
                onClick = { onDeleteEntry(entry.id) }) {
                Icon(imageVector = Icons.Outlined.Delete, contentDescription = "Delete entry")
            }
        }
        Divider()
    }
}

@Preview
@Composable
fun EntryRowPreview() {
    EntryRow(entry = Entry(0, LocalDate.now(), "We had beer together...", 21), {}, {})
}