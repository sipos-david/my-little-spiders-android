package dev.shipi.mylittlespiders.presentation.friend.add

import androidx.compose.runtime.Composable
import dev.shipi.mylittlespiders.presentation.friend.FriendForm

@Composable
fun AddFriendScreen(viewModel: AddFriendViewModel) {
    FriendForm(viewModel.formViewModel)
}