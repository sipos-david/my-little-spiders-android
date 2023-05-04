package dev.shipi.mylittlespiders.presentation.friend.add

import androidx.compose.runtime.Composable
import dev.shipi.mylittlespiders.presentation.friend.FriendForm

@Composable
fun AddFriendScreen(viewModel: AddFriendViewModel, onNavigateToListView: () -> Unit) {
    FriendForm(
        viewModel = viewModel.formViewModel,
        title = "Add friend",
        submit = "Let's become friends!",
        onSubmit = {
            viewModel.addFriend()
            onNavigateToListView()
        },
        onNavigateBack = onNavigateToListView
    )
}