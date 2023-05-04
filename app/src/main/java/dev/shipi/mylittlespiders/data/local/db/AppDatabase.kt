package dev.shipi.mylittlespiders.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.shipi.mylittlespiders.data.local.dao.EntryDao
import dev.shipi.mylittlespiders.data.local.dao.RoommateDao
import dev.shipi.mylittlespiders.data.local.model.Entry
import dev.shipi.mylittlespiders.data.local.model.Roommate

@Database(entities = [Roommate::class, Entry::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun roommateDao(): RoommateDao

    abstract fun entryDao(): EntryDao
}