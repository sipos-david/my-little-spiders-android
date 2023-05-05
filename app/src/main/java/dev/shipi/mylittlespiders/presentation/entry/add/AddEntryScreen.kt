package dev.shipi.mylittlespiders.presentation.entry.add

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import dev.shipi.mylittlespiders.presentation.entry.EntryForm


@Composable
fun AddEntryScreen(viewModel: AddEntryViewModel, onNavigateToFriend: () -> Unit) {
    val state by viewModel.state.collectAsState()
    EntryForm(
        state = state,
        title = "Add entry",
        submit = "Ok",
        onSubmit = {
            viewModel.addEntry()
            onNavigateToFriend()
        },
        onNavigateBack = onNavigateToFriend
    )
}