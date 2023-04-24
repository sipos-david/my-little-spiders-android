package dev.shipi.mylittlespiders.domain.model

import java.util.Date

data class Entry(val id: Long, val date: Date, val text: String, val respect: Int)
