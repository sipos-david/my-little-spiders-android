package dev.shipi.mylittlespiders.presentation.entry.update

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import dev.shipi.mylittlespiders.lib.presentation.ViewState
import dev.shipi.mylittlespiders.presentation.entry.EntryForm
import dev.shipi.mylittlespiders.presentation.entry.EntryFormViewModel

@Composable
fun UpdateEntryScreen(state: ViewState<EntryFormViewModel>) {
    when (state) {
        is ViewState.Data -> {
            if (state.isNetworkAvailable) {
                EntryForm(state.data)
            }
            Text(text = "Network unavailable!")
        }

        is ViewState.Error -> Text(text = state.e.message.toString())
        ViewState.Loading -> CircularProgressIndicator()
    }
}