package dev.shipi.mylittlespiders.presentation.entry.add

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import dev.shipi.mylittlespiders.components.ErrorScreen
import dev.shipi.mylittlespiders.components.LoadingScreen
import dev.shipi.mylittlespiders.lib.presentation.ViewState
import dev.shipi.mylittlespiders.presentation.entry.EntryForm
import dev.shipi.mylittlespiders.presentation.entry.EntryFormViewModel

@Composable
fun AddEntryScreen(viewModel: AddEntryViewModel, onNavigateToFriend: () -> Unit) {
    val state by viewModel.state.collectAsState()
    AddEntryView(state) {
        viewModel.addEntry()
        onNavigateToFriend()
    }
}

@Composable
fun AddEntryView(state: ViewState<EntryFormViewModel>, onSubmit: () -> Unit) {
    when (state) {
        is ViewState.Data -> {
            if (!state.isNetworkAvailable) {
                return Text(text = "Network unavailable!")
            }
            EntryForm(
                state.data,
                "Add entry",
                "Ok",
                onSubmit
            )
        }

        is ViewState.Error -> ErrorScreen(state.e.message)
        ViewState.Loading -> LoadingScreen()
    }
}