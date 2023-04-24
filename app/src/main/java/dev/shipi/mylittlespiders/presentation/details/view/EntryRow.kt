package dev.shipi.mylittlespiders.presentation.details.view

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dev.shipi.mylittlespiders.domain.model.Entry
import java.util.Date

@Composable
fun EntryRow(entry: Entry) {
    Column {
        Text(text = entry.text)
        Text(text = entry.date.toString())
        Text(text = entry.respect.toString())
    }
}

@Preview
@Composable
fun EntryRowPreview() {
    EntryRow(entry = Entry(0, Date(), "We had beer together...", 21))
}