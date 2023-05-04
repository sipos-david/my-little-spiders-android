package dev.shipi.api

import kotlinx.serialization.Serializable

@Serializable
data class RoommateForm(
    val name: String,
    val location: String,
    val nightmares: Long? = null
)
