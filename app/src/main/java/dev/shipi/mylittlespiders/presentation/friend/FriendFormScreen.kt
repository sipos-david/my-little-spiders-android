package dev.shipi.mylittlespiders.presentation.friend

import androidx.compose.runtime.Composable
import dev.shipi.mylittlespiders.components.widgets.NetworkNotAvailableWidget
import dev.shipi.mylittlespiders.components.ViewState
import dev.shipi.mylittlespiders.components.screens.ErrorScreen
import dev.shipi.mylittlespiders.components.screens.LoadingScreen

@Composable
fun FriendFormScreen(
    state: ViewState<FriendFormViewModel>,
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
            FriendForm(
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