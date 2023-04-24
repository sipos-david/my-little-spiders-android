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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.marosseleng.compose.material3.datetimepickers.date.ui.dialog.DatePickerDialog
import dev.shipi.mylittlespiders.lib.createString

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun EntryForm(
    viewModel: EntryFormViewModel,
    title: String,
    submit: String,
    onSubmit: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    var isDialogShown: Boolean by rememberSaveable {
        mutableStateOf(false)
    }
    if (isDialogShown) {
        DatePickerDialog(
            onDismissRequest = { isDialogShown = false },
            onDateChange = {
                viewModel.setDate(it)
                isDialogShown = false
            },
            // Optional but recommended parameter to provide the title for the dialog
            title = { Text(text = "Select date") }
        )
    }
    Column {
        Text(text = title)
        Text(text = state.date.value.toString())
        Button(onClick = { isDialogShown = true }) {
            Text(text = "Edit")
        }
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
        Button(onClick = onSubmit, enabled = !state.hasErrors) {
            Text(text = submit)
        }
    }
}

@Preview
@Composable
fun EntryFormPreview() {
    EntryForm(EntryFormViewModel(0), "Title", "Submit") { }
}