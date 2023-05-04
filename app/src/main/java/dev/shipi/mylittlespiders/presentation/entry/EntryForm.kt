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
import dev.shipi.mylittlespiders.components.DateInputWidget
import dev.shipi.mylittlespiders.lib.createString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryForm(
    viewModel: EntryFormViewModel,
    title: String,
    submit: String,
    onSubmit: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()

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
            DateInputWidget(state = state.date.value, setState = viewModel::setDate)
            OutlinedTextField(
                value = state.text.value,
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                onValueChange = viewModel::setText,
                label = { Text("Text") },
                isError = state.text.hasError,
            )
            OutlinedTextField(
                value = createString(state.respect.value),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                onValueChange = viewModel::setRespect,
                label = { Text("Respect") },
                isError = state.respect.hasError,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            )
            Button(
                modifier = Modifier.padding(top = 64.dp),
                onClick = onSubmit,
                enabled = !state.hasErrors
            ) {
                Text(text = submit)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EntryFormPreview() {
    EntryForm(EntryFormViewModel(0), "Title", "Submit", { }, { })
}