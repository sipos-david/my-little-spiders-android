package dev.shipi.mylittlespiders.presentation.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import dev.shipi.mylittlespiders.domain.model.Friend
import dev.shipi.mylittlespiders.lib.presentation.ViewState
import dev.shipi.mylittlespiders.presentation.list.view.FriendRow
import java.lang.Exception

@Composable
fun ListScreen(
    state: ViewState<List<Friend>>,
    onListRefresh: () -> Unit,
    onViewFriend: (Long) -> Unit,
    onAddNewFriend: () -> Unit,
) {
    when (state) {
        is ViewState.Loading -> CircularProgressIndicator()
        is ViewState.Error -> Text(text = "${state.e.message}")
        is ViewState.Data -> Column {
            if (!state.isNetworkAvailable) {
                Text(text = "Network is unavailable!")
            }
            Button(onClick = onListRefresh) {
                Text(text = "Refresh from network")
            }
            FloatingActionButton(onClick = onAddNewFriend) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "Add new friend!",
                )
            }
            LazyColumn {
                items(state.data) { friend ->
                    FriendRow(friend, onViewFriend)
                }
            }
        }
    }
}

class ListStatePreviewProvider : PreviewParameterProvider<ViewState<List<Friend>>> {
    private val friends = listOf(
        Friend(0, "Luigi", "Kitchen"),
        Friend(1, "D'aand'mo Av'ugd", "cellar"),
        Friend(2, "Quentin Tarantula", "Movie room"),
        Friend(3, "Peter Parker", "Guest room")
    )
    override val values = sequenceOf(
        ViewState.Loading,
        ViewState.Error(Exception("Preview exception")),
        ViewState.Data(friends, false),
        ViewState.Data(listOf(), false),
        ViewState.Data(friends, true),
        ViewState.Data(listOf(), true),
    )
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun ListScreenPreview(@PreviewParameter(ListStatePreviewProvider::class) state: ViewState<List<Friend>>) {
    ListScreen(state = state, {}, {}, {})
}