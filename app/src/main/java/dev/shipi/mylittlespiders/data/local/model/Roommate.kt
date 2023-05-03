package dev.shipi.mylittlespiders.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "roommates")
data class Roommate(
    @PrimaryKey val id: Long,
    val name: String,
    val location: String,
    @ColumnInfo(defaultValue = "0") val nightmares: Int
)
