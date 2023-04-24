package dev.shipi.mylittlespiders.domain.model

import java.time.LocalDate

data class Entry(val id: Long, val date: LocalDate, val text: String, val respect: Int)
