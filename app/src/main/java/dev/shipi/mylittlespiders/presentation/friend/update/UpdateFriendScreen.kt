package dev.shipi.mylittlespiders.presentation.friend.update

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import dev.shipi.mylittlespiders.components.ErrorScreen
import dev.shipi.mylittlespiders.components.LoadingScreen
import dev.shipi.mylittlespiders.components.NetworkNotAvailableWidget
import dev.shipi.mylittlespiders.lib.presentation.ViewState
import dev.shipi.mylittlespiders.presentation.friend.FriendForm
import dev.shipi.mylittlespiders.presentation.friend.FriendFormViewModel

@Composable
fun UpdateFriendScreen(
    viewModel: UpdateFriendViewModel,
    navigateToFriendDetails: (Long?) -> Unit,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    UpdateFriendView(state = state, {
        viewModel.updateFriend()
        if (state is ViewState.Data) {
            navigateToFriendDetails(
                (state as ViewState.Data<FriendFormViewModel>).data.state.value.friendId
            )
        } else {
            onNavigateBack()
        }
    }, onNavigateBack)
}

@Composable
fun UpdateFriendView(
    state: ViewState<FriendFormViewModel>,
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
                "Edit roommate",
                "Ok",
                onSubmit,
                onNavigateBack
            )
        }

        is ViewState.Error -> ErrorScreen(state.e.message)
        ViewState.Loading -> LoadingScreen()
    }
}