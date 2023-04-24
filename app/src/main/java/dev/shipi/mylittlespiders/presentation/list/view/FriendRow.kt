package dev.shipi.mylittlespiders.presentation.list.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.shipi.mylittlespiders.domain.model.Friend


@Composable
fun FriendRow(
    friend: Friend,
    onDeleteFriend: (Long) -> Unit,
    onNavigateToViewFriend: (Long) -> Unit,
    onNavigateToUpdateFriend: (Long) -> Unit,
) {
    Column(Modifier.clickable { onNavigateToViewFriend(friend.id) }) {
        Text(text = friend.name)
        Text(text = friend.location)
        Row {
            Button(onClick = { onNavigateToUpdateFriend(friend.id) }) {
                Text(text = "Edit")
            }
            Button(onClick = { onDeleteFriend(friend.id) }) {
                Text(text = "Delete")
            }
        }
    }
}

@Preview
@Composable
fun FriendRowPreview() {
    FriendRow(friend = Friend(0, "Quentin Tarantula", "Movie room"), {}, {}, {})
}