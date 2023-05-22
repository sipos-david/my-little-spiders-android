package dev.shipi.mylittlespiders.data.network.fake

import dev.shipi.mylittlespiders.data.network.client.apis.EntriesApi
import dev.shipi.mylittlespiders.data.network.client.models.Entry
import dev.shipi.mylittlespiders.data.network.client.models.EntryForm
import retrofit2.Response
import java.time.LocalDate

class FakeEntriesApi(private val db: FakeDb) : EntriesApi {
    override suspend fun addEntry(roommateId: Long, entryForm: EntryForm?): Response<Entry> {
        val idx = db.roommates.indexOfFirst { it.id == roommateId }
        if (idx < 0) {
            return error(404)
        }
        if (entryForm?.date == null) {
            return error(400)
        }
        val created = Entry(
            db.id,
            entryForm.date ?: LocalDate.now(),
            entryForm.text,
            entryForm.respect ?: 0
        )
        val edited = db.roommates[idx].copy(
            propertyEntries = db.roommates[idx].propertyEntries?.plus(created)
        )
        db.roommates.apply {
            this[idx] = edited
        }
        return success(200, created)
    }

    override suspend fun deleteEntry(roommateId: Long, entryId: Long): Response<Entry> {
        val roommateIdx = db.roommates.indexOfFirst { it.id == roommateId }
        if (roommateIdx < 0) {
            return error(404)
        }
        val roommate = db.roommates[roommateIdx]
        val entryIndex = roommate.propertyEntries?.indexOfFirst { it.id == entryId }
        if (entryIndex == null || entryIndex < 0) {
            return error(404)
        }
        val newList = roommate.propertyEntries!!.toMutableList()
        val removed = newList.removeAt(entryIndex)
        val edited = roommate.copy(propertyEntries = newList)
        db.roommates.apply {
            this[roommateIdx] = edited
        }
        return success(200, removed)
    }

    override suspend fun updateEntry(
        roommateId: Long, entryId: Long, entryForm: EntryForm?
    ): Response<Entry> {
        if (entryForm == null) {
            return error(400)
        }
        val roommateIdx = db.roommates.indexOfFirst { it.id == roommateId }
        if (roommateIdx < 0) {
            return error(404)
        }
        val roommate = db.roommates[roommateIdx]
        val entryIndex = roommate.propertyEntries?.indexOfFirst { it.id == entryId }
        if (entryIndex == null || entryIndex < 0) {
            return error(404)
        }
        val edited = roommate.propertyEntries!![entryIndex].copy(
            date = entryForm.date,
            text = entryForm.text,
            respect = entryForm.respect
        )
        db.roommates.apply {
            this[roommateIdx] =
                roommate.copy(propertyEntries = roommate.propertyEntries!!.toMutableList().apply {
                    this[entryIndex] = edited
                })
        }
        return success(200, edited)
    }
}

