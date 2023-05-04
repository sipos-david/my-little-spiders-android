package dev.shipi.mylittlespiders.presentation.entry.update

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
fun UpdateEntryScreen(viewModel: UpdateEntryViewModel, onNavigateToFriend: () -> Unit) {
    val state by viewModel.state.collectAsState()
    UpdateEntryView(state) {
        viewModel.updateEntry()
        onNavigateToFriend()
    }
}

@Composable
fun UpdateEntryView(state: ViewState<EntryFormViewModel>, onSubmit: () -> Unit) {
    when (state) {
        is ViewState.Data -> {
            if (!state.isNetworkAvailable) {
                return Text(text = "Network unavailable!")
            }
            EntryForm(
                state.data,
                "Edit entry",
                "Ok",
                onSubmit
            )
        }

        is ViewState.Error -> ErrorScreen(state.e.message)
        ViewState.Loading -> LoadingScreen()
    }
}