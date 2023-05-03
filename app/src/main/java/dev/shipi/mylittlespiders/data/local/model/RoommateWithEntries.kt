package dev.shipi.mylittlespiders.data.local.model

import androidx.room.Embedded
import androidx.room.Relation


data class RoommateWithEntries(
    @Embedded val roommate: Roommate,
    @Relation(
        parentColumn = "id",
        entityColumn = "roommateId"
    )
    val entries: List<Entry>
)
