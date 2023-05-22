package dev.shipi.mylittlespiders.data.local.fake

import dev.shipi.mylittlespiders.data.local.dao.RoommateDao
import dev.shipi.mylittlespiders.data.local.model.Roommate
import dev.shipi.mylittlespiders.data.local.model.RoommateWithEntries

class FakeRoommateDao(private val db: FakeDb) : RoommateDao {
    override suspend fun getRoommatesWithEntries(): List<RoommateWithEntries> {
        return db.roommates.map {
            RoommateWithEntries(
                roommate = it,
                entries = db.entries.filter { e -> e.roommateId == it.id }
            )
        }
    }

    override suspend fun getRoommateDetails(id: Long): RoommateWithEntries? {
        val roommate = db.roommates.firstOrNull { it.id == id } ?: return null
        return RoommateWithEntries(
            roommate = roommate,
            entries = db.entries.filter { e -> e.roommateId == id }
        )
    }

    override suspend fun getAllRoommatesNotIn(ids: List<Long>): List<Long> {
        return db.roommates.filter { it.id !in ids }.map { it.id }
    }

    override suspend fun insert(vararg roommate: Roommate) {
        roommate.forEach { save(it) }
    }

    override suspend fun insertAll(roommates: List<Roommate>) {
        roommates.forEach { save(it) }
    }

    override suspend fun update(vararg roommate: Roommate) {
        roommate.forEach {
            val index = db.roommates.indexOfFirst { r -> r.id == it.id }
            if (index != -1) {
                db.roommates[index] = it
            }
        }
    }

    override suspend fun delete(vararg roommate: Roommate) {
        roommate.forEach {
            db.roommates.remove(it)
        }
    }

    override suspend fun deleteAllById(ids: List<Long>) {
        db.roommates.removeAll { it.id in ids }
    }

    private fun save(roommate: Roommate) {
        val index = db.roommates.indexOfFirst { it.id == roommate.id }
        if (index != -1) {
            db.roommates[index] = roommate
        } else {
            db.roommates.add(roommate)
        }
    }
}