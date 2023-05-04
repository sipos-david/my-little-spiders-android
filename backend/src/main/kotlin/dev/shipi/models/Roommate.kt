package dev.shipi.models

import kotlinx.serialization.Serializable

@Serializable
data class Roommate(
    val id: Long,
    val name: String,
    val location: String,
    val nightmares: Long,
    val entries: List<Entry>
)
