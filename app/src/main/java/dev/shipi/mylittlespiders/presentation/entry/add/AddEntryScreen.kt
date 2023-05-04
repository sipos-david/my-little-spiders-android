package dev.shipi.mylittlespiders.presentation.entry.add

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import dev.shipi.mylittlespiders.components.ErrorScreen
import dev.shipi.mylittlespiders.components.LoadingScreen
import dev.shipi.mylittlespiders.components.NetworkNotAvailableWidget
import dev.shipi.mylittlespiders.lib.presentation.ViewState
import dev.shipi.mylittlespiders.presentation.entry.EntryForm
import dev.shipi.mylittlespiders.presentation.entry.EntryFormViewModel

@Composable
fun AddEntryScreen(viewModel: AddEntryViewModel, onNavigateToFriend: () -> Unit) {
    val state by viewModel.state.collectAsState()
    AddEntryView(state, {
        viewModel.addEntry()
        onNavigateToFriend()
    }, onNavigateToFriend)
}

@Composable
fun AddEntryView(
    state: ViewState<EntryFormViewModel>,
    onSubmit: () -> Unit,
    onNavigateBack: () -> Unit
) {
    when (state) {
        is ViewState.Data -> {
            if (!state.isNetworkAvailable) {
                return NetworkNotAvailableWidget()
            }
            EntryForm(
                state.data,
                "Add entry",
                "Ok",
                onSubmit,
                onNavigateBack
            )
        }

        is ViewState.Error -> ErrorScreen(state.e.message)
        ViewState.Loading -> LoadingScreen()
    }
}