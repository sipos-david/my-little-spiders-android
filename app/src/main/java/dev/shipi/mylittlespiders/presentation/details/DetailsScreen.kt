package dev.shipi.mylittlespiders.presentation.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import dev.shipi.mylittlespiders.components.screens.ErrorScreen
import dev.shipi.mylittlespiders.components.screens.LoadingScreen
import dev.shipi.mylittlespiders.components.widgets.NetworkNotAvailableWidget
import dev.shipi.mylittlespiders.domain.model.Entry
import dev.shipi.mylittlespiders.domain.model.FriendDetails
import dev.shipi.mylittlespiders.components.ViewState
import dev.shipi.mylittlespiders.presentation.details.view.EntryRow
import java.lang.Exception
import java.time.LocalDate

@Composable
fun DetailsScreen(
    viewModel: DetailsViewModel,
    onNavigateToAddEntry: () -> Unit,
    onNavigateToEditEntry: (Long) -> Unit,
    onNavigateBack: () -> Unit,
) {
    val state by viewModel.state.collectAsState()
    Details(
        state = state,
        onDeleteEntry = viewModel::onDeleteEntry,
        onNavigateToAddEntry = onNavigateToAddEntry,
        onNavigateToEditEntry = onNavigateToEditEntry,
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Details(
    state: ViewState<FriendDetails>,
    onDeleteEntry: (Long) -> Unit,
    onNavigateToAddEntry: () -> Unit,
    onNavigateToEditEntry: (Long) -> Unit,
    onNavigateBack: () -> Unit,
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
                    onClick = onNavigateToAddEntry
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = "Add new entry!",
                    )
                }
            }
            Column {
                LargeTopAppBar(
                    title = {
                        Column {
                            Text(text = "Roommate", style = MaterialTheme.typography.titleMedium)
                            Text(text = state.data.name)
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                tint = MaterialTheme.colorScheme.onBackground,
                                imageVector = Icons.Outlined.ArrowBack,
                                contentDescription = "Navigate back"
                            )
                        }
                    }
                )
                if (!state.isNetworkAvailable) {
                    NetworkNotAvailableWidget()
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier
                                .width(48.dp)
                                .height(48.dp)
                                .padding(end = 8.dp),
                            tint = MaterialTheme.colorScheme.primary,
                            imageVector = Icons.Outlined.LocationOn,
                            contentDescription = "Location icon"
                        )
                        Column {
                            Text(text = "Location", style = MaterialTheme.typography.titleMedium)
                            Text(
                                text = state.data.location,
                                style = MaterialTheme.typography.headlineSmall
                            )
                        }
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier
                                .width(48.dp)
                                .height(48.dp)
                                .padding(end = 8.dp),
                            tint = MaterialTheme.colorScheme.primary,
                            imageVector = Icons.Outlined.Face,
                            contentDescription = "Happy image"
                        )
                        Column {
                            Text(
                                text = "Nightmares",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = state.data.nightmares.toString(),
                                style = MaterialTheme.typography.headlineSmall
                            )
                        }
                    }
                }
                Text(
                    text = "Entries",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp, start = 16.dp)
                )
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    if (state.data.entries.isEmpty()) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "No entries added 😞",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                    state.data.entries.forEach {
                        EntryRow(
                            entry = it,
                            onDeleteEntry = onDeleteEntry,
                            onNavigateToEditEntry = onNavigateToEditEntry
                        )
                    }
                }
            }
        }
    }
}

class DetailsStatePreviewProvider : PreviewParameterProvider<ViewState<FriendDetails>> {
    private val details = FriendDetails(
        2, "Quentin Tarantula", "Movie room", 1, listOf(
            Entry(0, LocalDate.now(), "He suddenly appeared with a movie", 234),
            Entry(1, LocalDate.now(), "He made a website", 21),
            Entry(
                2,
                LocalDate.now(),
                "I finally had the courage to ask him how do other spiders find a partner? He said they usually meet on the web!",
                12
            ),
            Entry(3, LocalDate.now(), "", 25),
        )
    )

    private val emptyDetails = FriendDetails(
        2, "Quentin Tarantula", "Movie room", 1, listOf()
    )

    override val values = sequenceOf(
        ViewState.Loading,
        ViewState.Error(Exception("Preview exception")),
        ViewState.Data(details, false),
        ViewState.Data(details, true),
        ViewState.Data(emptyDetails, false),
        ViewState.Data(emptyDetails, true),
    )
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun DetailsPreview(@PreviewParameter(DetailsStatePreviewProvider::class) state: ViewState<FriendDetails>) {
    Details(state = state, {}, {}, {}, {})
}