package dev.shipi.mylittlespiders.domain.model

data class FriendDetails(
    val id: Long,
    val name: String,
    val location: String,
    val nightmares: Int,
    val entries: List<Entry>
)
