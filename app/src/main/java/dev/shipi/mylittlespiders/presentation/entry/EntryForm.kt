package dev.shipi.mylittlespiders.presentation.entry

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.shipi.mylittlespiders.components.ViewState
import dev.shipi.mylittlespiders.components.screens.ErrorScreen
import dev.shipi.mylittlespiders.components.screens.LoadingScreen
import dev.shipi.mylittlespiders.components.widgets.DateInputWidget

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryForm(
    state: ViewState<EntryFormViewModel>,
    title: String,
    submit: String,
    onSubmit: () -> Unit,
    onNavigateBack: () -> Unit
) {
    when (state) {
        is ViewState.Data -> {
            val viewModel = state.data
            val form by state.data.state.collectAsState()

            Column {
                LargeTopAppBar(
                    title = {
                        Text(text = title)
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
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    DateInputWidget(state = form.date.value, setState = viewModel::setDate)
                    OutlinedTextField(
                        value = form.text.value,
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 32.dp),
                        onValueChange = viewModel::setText,
                        label = { Text("Text") },
                        isError = form.text.hasError,
                    )
                    OutlinedTextField(
                        value = (form.respect.value ?: "").toString(),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 32.dp),
                        onValueChange = viewModel::setRespect,
                        label = { Text("Respect") },
                        isError = form.respect.hasError,
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    )
                    Button(
                        modifier = Modifier.padding(top = 64.dp),
                        onClick = onSubmit,
                        enabled = !form.hasErrors && state.isNetworkAvailable
                    ) {
                        Text(text = submit)
                    }

                }

            }
        }

        is ViewState.Error -> ErrorScreen(state.e.message)
        ViewState.Loading -> LoadingScreen()
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EntryFormPreview() {
    EntryForm(ViewState.Data(EntryFormViewModel(0), true), "Title", "Submit", { }, { })
}