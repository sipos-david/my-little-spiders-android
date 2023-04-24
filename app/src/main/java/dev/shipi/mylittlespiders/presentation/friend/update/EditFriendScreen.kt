package dev.shipi.mylittlespiders.presentation.friend.update

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import dev.shipi.mylittlespiders.lib.presentation.ViewState
import dev.shipi.mylittlespiders.presentation.friend.FriendForm
import dev.shipi.mylittlespiders.presentation.friend.FriendFormViewModel

@Composable
fun EditFriendScreen(state: ViewState<FriendFormViewModel>) {
    when (state) {
        is ViewState.Data -> {
            if (state.isNetworkAvailable) {
                FriendForm(state.data)
            }
            Text(text = "Network unavailable!")
        }

        is ViewState.Error -> Text(text = state.e.message.toString())
        ViewState.Loading -> CircularProgressIndicator()
    }
}