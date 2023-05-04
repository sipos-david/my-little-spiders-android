package dev.shipi.mylittlespiders.presentation.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import dev.shipi.mylittlespiders.components.ErrorScreen
import dev.shipi.mylittlespiders.components.LoadingScreen
import dev.shipi.mylittlespiders.domain.model.Friend
import dev.shipi.mylittlespiders.lib.presentation.ViewState
import dev.shipi.mylittlespiders.presentation.list.view.FriendRow
import java.lang.Exception

@Composable
fun ListScreen(
    viewModel: ListViewModel,
    onNavigateToViewFriend: (Long) -> Unit,
    onNavigateToUpdateFriend: (Long) -> Unit,
    onNavigateToAddFriend: () -> Unit,
) {
    val state by viewModel.state.collectAsState()
    ListView(
        state = state,
        onListRefresh = viewModel::refreshList,
        onDeleteFriend = viewModel::onDeleteFriend,
        onNavigateToViewFriend = onNavigateToViewFriend,
        onNavigateToUpdateFriend = onNavigateToUpdateFriend,
        onNavigateToAddFriend = onNavigateToAddFriend
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListView(
    state: ViewState<List<Friend>>,
    onListRefresh: () -> Unit,
    onDeleteFriend: (Long) -> Unit,
    onNavigateToViewFriend: (Long) -> Unit,
    onNavigateToUpdateFriend: (Long) -> Unit,
    onNavigateToAddFriend: () -> Unit,
) {
    when (state) {
        is ViewState.Loading -> LoadingScreen()
        is ViewState.Error -> ErrorScreen(state.e.message)
        is ViewState.Data -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomEnd
            ) {
                FloatingActionButton(
                    modifier = Modifier.padding(bottom = 16.dp, end = 16.dp),
                    onClick = onNavigateToAddFriend
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = "Add new friend!",
                    )
                }
            }
            Column {
                LargeTopAppBar(
                    title = {
                        Text(
                            text = "My Little Spiders",
                            style = MaterialTheme.typography.headlineMedium
                        )
                    },
                    actions = {
                        IconButton(onClick = onListRefresh) {
                            Icon(
                                imageVector = Icons.Outlined.Refresh,
                                contentDescription = "Refresh list from network."
                            )
                        }
                    }
                )
                if (!state.isNetworkAvailable) {
                    Text(text = "Network is unavailable!")
                }
                LazyColumn(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp)) {
                    items(state.data) { friend ->
                        FriendRow(
                            friend,
                            onDeleteFriend,
                            onNavigateToViewFriend,
                            onNavigateToUpdateFriend
                        )
                    }
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
fun ListViewPreview(@PreviewParameter(ListStatePreviewProvider::class) state: ViewState<List<Friend>>) {
    ListView(state = state, {}, {}, {}, {}, {})
}