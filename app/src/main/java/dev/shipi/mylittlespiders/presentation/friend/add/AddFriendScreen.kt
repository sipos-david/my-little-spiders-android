package dev.shipi.mylittlespiders.presentation.friend.add

import androidx.compose.runtime.Composable
import dev.shipi.mylittlespiders.presentation.friend.FriendForm

@Composable
fun AddFriendScreen(viewModel: AddFriendViewModel, onNavigateToListView: () -> Unit) {
    FriendForm(viewModel.formViewModel, "Add friend", "Let's become friends!") {
        viewModel.addFriend()
        onNavigateToListView()
    }
}