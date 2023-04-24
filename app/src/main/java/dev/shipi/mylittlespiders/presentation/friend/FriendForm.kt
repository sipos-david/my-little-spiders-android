@file:OptIn(ExperimentalMaterial3Api::class)

package dev.shipi.mylittlespiders.presentation.friend

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

@Composable
fun FriendForm(
    viewModel: FriendFormViewModel,
    title: String,
    submit: String,
    onSubmit: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    Column {
        Text(text = title)
        OutlinedTextField(
            value = state.name.value,
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = viewModel::setName,
            label = { Text("Name") },
            isError = state.name.hasError,
        )
        OutlinedTextField(
            value = state.location.value,
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = viewModel::setLocation,
            label = { Text("Location") },
            isError = state.location.hasError,
        )
        OutlinedTextField(
            value = createString(state.nightmares.value),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = viewModel::setNightmares,
            label = { Text("Nightmares") },
            isError = state.nightmares.hasError,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        )
        Button(onClick = onSubmit, enabled = !state.hasErrors) {
            Text(text = submit)
        }
    }
}

@Preview
@Composable
fun FriendFormPreview() {
    FriendForm(FriendFormViewModel(), "Title", "Submit") { }
}