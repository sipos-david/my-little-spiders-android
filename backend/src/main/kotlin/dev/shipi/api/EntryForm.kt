package dev.shipi.api

import kotlinx.serialization.Serializable

@Serializable
data class EntryForm(val date: String? = null, val text: String, val respect: Long? = null)
