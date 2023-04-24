package dev.shipi.mylittlespiders.presentation.entry

import dev.shipi.mylittlespiders.lib.presentation.Input
import java.util.Date

data class EntryFormState(
    val title: String,
    val submit: String,

    val date: Input.Value<Date?>,
    val text: Input.Value<String>,
    val respect: Input.Value<Int>,
    val hasErrors: Boolean = true
)