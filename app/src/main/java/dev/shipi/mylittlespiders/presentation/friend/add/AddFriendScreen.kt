package dev.shipi.mylittlespiders.presentation.friend.add

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import dev.shipi.mylittlespiders.presentation.friend.FriendForm

@Composable
fun AddFriendScreen(viewModel: AddFriendViewModel, onNavigateToListView: () -> Unit) {
    val state by viewModel.state.collectAsState()
    FriendForm(
        state = state,
        title = "Add friend",
        submit = "Let's become friends!",
        onSubmit = {
            viewModel.addFriend()
            onNavigateToListView()
        },
        onNavigateBack = onNavigateToListView
    )
}