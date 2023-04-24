package dev.shipi.mylittlespiders.presentation.details.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dev.shipi.mylittlespiders.domain.model.Entry
import java.time.LocalDate

@Composable
fun EntryRow(entry: Entry, onDeleteEntry: (Long) -> Unit, onNavigateToEditEntry: (Long) -> Unit) {
    Column {
        Text(text = entry.text)
        Text(text = entry.date.toString())
        Text(text = entry.respect.toString())
        Row {
            Button(onClick = { onNavigateToEditEntry(entry.id) }) {
                Text(text = "Edit")
            }
            Button(onClick = { onDeleteEntry(entry.id) }) {
                Text(text = "Delete")
            }
        }
    }
}

@Preview
@Composable
fun EntryRowPreview() {
    EntryRow(entry = Entry(0, LocalDate.now(), "We had beer together...", 21), {}, {})
}