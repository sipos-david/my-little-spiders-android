package dev.shipi.mylittlespiders.presentation.entry

import dev.shipi.mylittlespiders.components.forms.Input
import java.time.LocalDate

data class EntryFormState(
    val friendId: Long,
    val date: Input.Value<LocalDate?>,
    val text: Input.Value<String>,
    val respect: Input.Value<Int?>,
    val hasErrors: Boolean = true,
    val entryId: Long? = null
)