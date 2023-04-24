package dev.shipi.mylittlespiders

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.shipi.mylittlespiders.presentation.details.DetailsScreen
import dev.shipi.mylittlespiders.presentation.details.DetailsViewModel

import dev.shipi.mylittlespiders.presentation.friend.add.AddFriendScreen
import dev.shipi.mylittlespiders.presentation.friend.add.AddFriendViewModel
import dev.shipi.mylittlespiders.presentation.list.ListScreen
import dev.shipi.mylittlespiders.presentation.list.ListViewModel
import dev.shipi.mylittlespiders.ui.theme.MyLittleSpidersTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val listViewModel: ListViewModel by viewModels()
    private val addFriendViewModel: AddFriendViewModel by viewModels()
    private val detailsViewModel: DetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            MyLittleSpidersTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(navController = navController, startDestination = "list") {
                        composable("list") {
                            ListScreen(viewModel = listViewModel,
                                onNavigateToViewFriend = {
                                    navController.navigate("details/$it")
                                },
                                onNavigateToAddFriend = {
                                    navController.navigate("add-friend")
                                }
                            )
                        }
                        composable("details/{friendId}") { backStackEntry ->
                            detailsViewModel.getFriendDetails(backStackEntry.arguments?.getString("friendId"))
                            DetailsScreen(detailsViewModel)
                        }
                        composable("add-friend") {
                            AddFriendScreen(viewModel = addFriendViewModel)
                        }
                    }
                }
            }
        }
    }
}
