package dev.shipi.mylittlespiders.presentation.entry.add

import androidx.compose.runtime.Composable
import dev.shipi.mylittlespiders.presentation.entry.EntryForm

@Composable
fun AddEntryScreen(viewModel: AddEntryViewModel) {
    EntryForm(viewModel.formViewModel)
}