package dev.shipi.mylittlespiders.presentation.list.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.shipi.mylittlespiders.domain.model.Friend


@Composable
fun FriendRow(
    friend: Friend,
    onDeleteFriend: (Long) -> Unit,
    onNavigateToViewFriend: (Long) -> Unit,
    onNavigateToUpdateFriend: (Long) -> Unit,
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable { onNavigateToViewFriend(friend.id) }
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp)
        )
        {
            Column {
                Text(text = friend.name, style = MaterialTheme.typography.headlineSmall)
                Text(text = friend.location, style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(Modifier.weight(1f))
            Button(
                modifier = Modifier
                    .width(48.dp)
                    .height(48.dp),
                contentPadding = PaddingValues(0.dp),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.secondary
                ),
                onClick = { onNavigateToUpdateFriend(friend.id) }) {
                Icon(imageVector = Icons.Outlined.Edit, contentDescription = "Edit friend")
            }
            Spacer(modifier = Modifier.padding(horizontal = 4.dp))
            Button(
                modifier = Modifier
                    .width(48.dp)
                    .height(48.dp),
                contentPadding = PaddingValues(0.dp),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.error
                ),
                onClick = { onDeleteFriend(friend.id) }) {
                Icon(imageVector = Icons.Outlined.Delete, contentDescription = "Edit friend")
            }
        }
        Divider()
    }
}

@Preview
@Composable
fun FriendRowPreview() {
    FriendRow(friend = Friend(0, "Quentin Tarantula", "Movie room"), {}, {}, {})
}