package dev.shipi.mylittlespiders.presentation.entry

import androidx.compose.runtime.Composable
import dev.shipi.mylittlespiders.components.ErrorScreen
import dev.shipi.mylittlespiders.components.LoadingScreen
import dev.shipi.mylittlespiders.components.NetworkNotAvailableWidget
import dev.shipi.mylittlespiders.lib.presentation.ViewState

@Composable
fun EntryFormScreen(
    state: ViewState<EntryFormViewModel>,
    title: String,
    submit: String,
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
                title,
                submit,
                onSubmit,
                onNavigateBack
            )
        }

        is ViewState.Error -> ErrorScreen(state.e.message)
        ViewState.Loading -> LoadingScreen()
    }
}