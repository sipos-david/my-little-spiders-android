package dev.shipi.mylittlespiders.presentation.friend

import dev.shipi.mylittlespiders.lib.presentation.Input

data class FriendFormState(
    val title: String,
    val submit: String,

    val name: Input.Value<String>,
    val location: Input.Value<String>,
    val nightmares: Input.Value<Int?>,
    val hasErrors: Boolean = true
)