package dev.shipi.mylittlespiders.presentation.entry

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import dev.shipi.mylittlespiders.lib.createString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryForm(
    viewModel: EntryFormViewModel
) {
    val state by viewModel.state.collectAsState()

    Column {
        Text(text = state.title)
        OutlinedTextField(
            value = state.text.value,
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = viewModel::setText,
            label = { Text("Text") },
            isError = state.text.hasError,
        )
        OutlinedTextField(
            value = createString(state.respect.value),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = viewModel::setRespect,
            label = { Text("Respect") },
            isError = state.respect.hasError,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        )
        Button(onClick = viewModel::onSubmit, enabled = !state.hasErrors) {
            Text(text = state.submit)
        }
    }
}

@Preview
@Composable
fun EntryFormPreview() {
    EntryForm(EntryFormViewModel("Title", "Submit") { _, _, _ -> })
}