package dev.shipi.mylittlespiders.presentation.friend.update

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import dev.shipi.mylittlespiders.lib.presentation.ViewState
import dev.shipi.mylittlespiders.presentation.friend.FriendForm
import dev.shipi.mylittlespiders.presentation.friend.FriendFormViewModel

@Composable
fun UpdateFriendScreen(viewModel: UpdateFriendViewModel, navigateToFriendDetails: () -> Unit) {
    val state by viewModel.state.collectAsState()
    UpdateFriendView(state = state) {
        viewModel.updateFriend()
        navigateToFriendDetails()
    }
}

@Composable
fun UpdateFriendView(state: ViewState<FriendFormViewModel>, onSubmit: () -> Unit) {
    when (state) {
        is ViewState.Data -> {
            if (!state.isNetworkAvailable) {
                return Text(text = "Network unavailable!")
            }
            FriendForm(
                state.data,
                "Edit roommate",
                "Ok",
                onSubmit
            )
        }

        is ViewState.Error -> Text(text = state.e.message.toString())
        ViewState.Loading -> CircularProgressIndicator()
    }
}