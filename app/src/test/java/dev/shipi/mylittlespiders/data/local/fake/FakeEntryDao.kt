package dev.shipi.mylittlespiders.data.local.fake

import dev.shipi.mylittlespiders.data.local.dao.EntryDao
import dev.shipi.mylittlespiders.data.local.model.Entry
import java.time.LocalDate

class FakeEntryDao(private val db: FakeDb) : EntryDao {
    override suspend fun insert(vararg entry: Entry) {
        entry.forEach { save(it) }
    }

    override suspend fun insertAll(entries: List<Entry>) {
        entries.forEach { save(it) }
    }

    override suspend fun update(entryId: Long, date: LocalDate, text: String, respect: Long): Int {
        val index = db.entries.indexOfFirst { it.id == entryId }
        if (index != -1) {
            db.entries[index] = db.entries[index].copy(date = date, text = text, respect = respect)
            return 1
        }
        return 0
    }

    override suspend fun deleteById(entryId: Long) {
        db.entries.removeAll { it.id == entryId }
    }

    override suspend fun deleteAllById(entryIds: List<Long>) {
        db.entries.removeAll { it.id in entryIds }
    }

    override suspend fun deleteByRoommate(roommateId: Long) {
        db.entries.removeAll { it.roommateId == roommateId }
    }

    override suspend fun deleteAllByRoommateIds(roommateIds: List<Long>) {
        db.entries.removeAll { it.roommateId in roommateIds }
    }

    override suspend fun deleteAllEntryNotIn(ids: List<Long>) {
        db.entries.retainAll { it.id in ids }
    }

    private fun save(entry: Entry) {
        val index = db.entries.indexOfFirst { it.id == entry.id }
        if (index != -1) {
            db.entries[index] = entry
        } else {
            db.entries.add(entry)
        }
    }
}