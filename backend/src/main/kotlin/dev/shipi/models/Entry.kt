package dev.shipi.models

import kotlinx.serialization.Serializable

@Serializable
data class Entry(val id: Long, val date: String, val text: String, val respect: Long)
