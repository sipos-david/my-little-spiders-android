package dev.shipi.mylittlespiders.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.shipi.mylittlespiders.data.local.model.Entry
import java.time.LocalDate

@Dao
interface EntryDao {
    @Insert
    suspend fun insert(vararg entry: Entry)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entries: List<Entry>)

    @Query("UPDATE entries SET date = :date, text = :text, respect = :respect WHERE id = :entryId")
    suspend fun update(entryId: Long, date: LocalDate, text: String, respect: Long): Int

    @Query("DELETE FROM entries WHERE id = :entryId")
    suspend fun deleteById(entryId: Long)

    @Query("DELETE FROM entries WHERE id IN (:entryIds)")
    suspend fun deleteAllById(entryIds: List<Long>)

    @Query("DELETE FROM entries WHERE roommateId = :roommateId")
    suspend fun deleteByRoommate(roommateId: Long)

    @Query("DELETE FROM entries WHERE roommateId IN (:roommateIds)")
    suspend fun deleteAllByRoommateIds(roommateIds: List<Long>)

    @Query("DELETE FROM entries WHERE id NOT IN (:ids)")
    suspend fun deleteAllEntryNotIn(ids: List<Long>)
}