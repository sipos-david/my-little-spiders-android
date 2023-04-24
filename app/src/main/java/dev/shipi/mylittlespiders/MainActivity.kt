package dev.shipi.mylittlespiders

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint

import dev.shipi.mylittlespiders.presentation.friend.add.AddFriendScreen
import dev.shipi.mylittlespiders.presentation.friend.add.AddFriendViewModel
import dev.shipi.mylittlespiders.presentation.list.ListScreen
import dev.shipi.mylittlespiders.presentation.list.ListViewModel
import dev.shipi.mylittlespiders.ui.theme.MyLittleSpidersTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val listViewModel: ListViewModel by viewModels()
    private val addFriendViewModel: AddFriendViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val listViewState by listViewModel.state.collectAsState()

            MyLittleSpidersTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        ListScreen(state = listViewState,
                            onListRefresh = listViewModel::refreshList,
                            onViewFriend = {},
                            onAddNewFriend = {}
                        )
                        AddFriendScreen(viewModel = addFriendViewModel)
                    }
                }
            }
        }
    }
}
