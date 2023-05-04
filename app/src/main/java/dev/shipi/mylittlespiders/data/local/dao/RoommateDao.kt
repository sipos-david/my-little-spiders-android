package dev.shipi.mylittlespiders.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import dev.shipi.mylittlespiders.data.local.model.Roommate
import dev.shipi.mylittlespiders.data.local.model.RoommateWithEntries

@Dao
interface RoommateDao {
    @Transaction
    @Query("SELECT * FROM roommates")
    suspend fun getRoommatesWithEntries(): List<RoommateWithEntries>

    @Transaction
    @Query("SELECT * FROM roommates WHERE id IS :id")
    suspend fun getRoommateDetails(id: Long): RoommateWithEntries?

    @Query("SELECT id FROM roommates WHERE id NOT IN(:ids)")
    suspend fun getAllRoommatesNotIn(ids: List<Long>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg roommate: Roommate)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(roommates: List<Roommate>)

    @Update
    suspend fun update(vararg roommate: Roommate)

    @Delete
    suspend fun delete(vararg roommate: Roommate)

    @Query("DELETE FROM roommates WHERE id IN (:ids)")
    suspend fun deleteAllById(ids: List<Long>)
}