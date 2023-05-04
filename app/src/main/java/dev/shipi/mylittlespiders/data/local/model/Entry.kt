package dev.shipi.mylittlespiders.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "entries")
data class Entry(
    @PrimaryKey val id: Long,
    val roommateId: Long,
    val date: LocalDate,
    val text: String,
    @ColumnInfo(defaultValue = "0") val respect: Long
)
