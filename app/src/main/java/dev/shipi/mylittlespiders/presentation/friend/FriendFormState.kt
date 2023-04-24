package dev.shipi.mylittlespiders.presentation.friend

import dev.shipi.mylittlespiders.lib.presentation.Input

data class FriendFormState(
    val name: Input.Value<String>,
    val location: Input.Value<String>,
    val nightmares: Input.Value<Int?>,
    val hasErrors: Boolean = true,
    val friendId: Long? = null
)