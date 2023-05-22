package dev.shipi.mylittlespiders.presentation.friend

import dev.shipi.mylittlespiders.components.forms.Input

data class FriendFormState(
    val name: Input.Value<String>,
    val location: Input.Value<String>,
    val nightmares: Input.Value<Int?>,
    val hasErrors: Boolean = true,
    val friendId: Long? = null
)