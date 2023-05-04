package dev.shipi.mylittlespiders

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.shipi.mylittlespiders.presentation.details.DetailsScreen
import dev.shipi.mylittlespiders.presentation.details.DetailsViewModel
import dev.shipi.mylittlespiders.presentation.entry.add.AddEntryScreen
import dev.shipi.mylittlespiders.presentation.entry.add.AddEntryViewModel
import dev.shipi.mylittlespiders.presentation.entry.update.UpdateEntryScreen
import dev.shipi.mylittlespiders.presentation.entry.update.UpdateEntryViewModel

import dev.shipi.mylittlespiders.presentation.friend.add.AddFriendScreen
import dev.shipi.mylittlespiders.presentation.friend.add.AddFriendViewModel
import dev.shipi.mylittlespiders.presentation.friend.update.UpdateFriendScreen
import dev.shipi.mylittlespiders.presentation.friend.update.UpdateFriendViewModel
import dev.shipi.mylittlespiders.presentation.list.ListScreen
import dev.shipi.mylittlespiders.presentation.list.ListViewModel
import dev.shipi.mylittlespiders.ui.theme.MyLittleSpidersTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val listViewModel: ListViewModel by viewModels()
    private val addFriendViewModel: AddFriendViewModel by viewModels()
    private val detailsViewModel: DetailsViewModel by viewModels()
    private val updateFriendViewModel: UpdateFriendViewModel by viewModels()
    private val addEntryViewModel: AddEntryViewModel by viewModels()
    private val updateEntryViewModel: UpdateEntryViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            MyLittleSpidersTheme {
                Scaffold { contentPadding ->
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(contentPadding),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        NavHost(navController = navController, startDestination = "list") {
                            composable("list") {
                                ListScreen(viewModel = listViewModel,
                                    onNavigateToViewFriend = {
                                        navController.navigate("details/$it")
                                    },
                                    onNavigateToUpdateFriend = {
                                        navController.navigate("update-friend/$it")
                                    },
                                    onNavigateToAddFriend = {
                                        navController.navigate("add-friend")
                                    }
                                )
                            }
                            navigation(
                                route = "details/{friendId}",
                                startDestination = "/",
                            ) {
                                composable(route = "/") { entry ->
                                    val parentEntry =
                                        remember(entry) { navController.getBackStackEntry("details/{friendId}") }
                                    val friendId = parentEntry.arguments?.getString("friendId")
                                    detailsViewModel.showFriendDetails(friendId)
                                    DetailsScreen(viewModel = detailsViewModel,
                                        onNavigateToAddEntry = {
                                            navController.navigate("/add")
                                        },
                                        onNavigateToEditEntry = {
                                            navController.navigate("/update/$it")
                                        }
                                    )
                                }
                                composable(route = "/add") { entry ->
                                    val parentEntry =
                                        remember(entry) { navController.getBackStackEntry("details/{friendId}") }
                                    val friendId = parentEntry.arguments?.getString("friendId")
                                    addEntryViewModel.showEntryDetails(friendId)
                                    AddEntryScreen(viewModel = addEntryViewModel) {
                                        navController.navigate("details/$friendId")
                                    }
                                }
                                composable(route = "/update/{entryId}") { entry ->
                                    val parentEntry =
                                        remember(entry) { navController.getBackStackEntry("details/{friendId}") }
                                    val friendId = parentEntry.arguments?.getString("friendId")
                                    updateEntryViewModel.showEntryDetails(
                                        friendId,
                                        entry.arguments?.getString("entryId")
                                    )
                                    UpdateEntryScreen(viewModel = updateEntryViewModel) {
                                        navController.navigate("details/$friendId")
                                    }
                                }
                            }
                            composable("add-friend") {
                                AddFriendScreen(viewModel = addFriendViewModel) {
                                    navController.navigate("list")
                                }
                            }
                            composable("update-friend/{friendId}") { backStackEntry ->
                                updateFriendViewModel.showFriendDetails(
                                    backStackEntry.arguments?.getString(
                                        "friendId"
                                    )
                                )
                                UpdateFriendScreen(viewModel = updateFriendViewModel) {
                                    navController.navigate("list")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
