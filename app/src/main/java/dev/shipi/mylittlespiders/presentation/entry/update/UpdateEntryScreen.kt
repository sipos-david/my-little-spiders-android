package dev.shipi.mylittlespiders.presentation.entry.update

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
fun UpdateEntryScreen(viewModel: UpdateEntryViewModel, onNavigateToFriend: () -> Unit) {
    val state by viewModel.state.collectAsState()
    UpdateEntryView(state, {
        viewModel.updateEntry()
        onNavigateToFriend()
    }, onNavigateToFriend)
}

@Composable
fun UpdateEntryView(
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
                "Edit entry",
                "Ok",
                onSubmit,
                onNavigateBack
            )
        }

        is ViewState.Error -> ErrorScreen(state.e.message)
        ViewState.Loading -> LoadingScreen()
    }
}