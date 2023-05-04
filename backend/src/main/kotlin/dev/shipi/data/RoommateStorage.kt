package dev.shipi.data

import dev.shipi.models.Entry
import dev.shipi.api.EntryForm
import dev.shipi.models.Roommate
import dev.shipi.api.RoommateForm
import java.time.LocalDate
import java.time.format.DateTimeParseException

object RoommateStorage {
    private var id: Long = 4

    private val roommates = mutableListOf(
        Roommate(0, "Luigi", "Kitchen", 4, listOf()),
        Roommate(1, "D'aand'mo Av'ugd", "cellar", 234, listOf()),
        Roommate(
            2, "Quentin Tarantula", "Movie room", 1, listOf(
                Entry(0, LocalDate.now().toString(), "He suddenly appeared with a movie", 234),
                Entry(1, LocalDate.now().toString(), "He made a website", 21),
                Entry(
                    2,
                    LocalDate.now().toString(),
                    "I finally had the courage to ask him how do other spiders find a partner? He said they usually meet on the web!",
                    12
                ),
                Entry(3, LocalDate.now().toString(), "", 25),
            )
        ),
        Roommate(3, "Peter Parker", "Guest room", 0, listOf())
    )

    fun getAllRoommates(): List<Roommate> {
        return roommates
    }

    fun getRoommate(id: Long): Roommate? {
        return roommates.firstOrNull {
            it.id == id
        }
    }

    fun addRoommate(form: RoommateForm): Roommate {
        val roommate = Roommate(id++, form.name, form.location, form.nightmares ?: 0, emptyList())
        roommates.add(roommate)
        return roommate
    }

    fun updateRoommate(id: Long, form: RoommateForm): Roommate? {
        val friend = getRoommate(id) ?: return null
        val updated = Roommate(
            friend.id,
            form.name,
            form.location,
            form.nightmares ?: 0,
            friend.entries
        )
        updateRoommate(updated)
        return updated
    }

    fun deleteRoommate(id: Long): Roommate? {
        val idx = roommates.indexOfFirst { it.id == id }
        if (idx < 0) {
            return null
        }
        return roommates.removeAt(idx)
    }

    fun addEntry(roommateId: Long, form: EntryForm): Entry? {
        val roommate = getRoommate(roommateId) ?: return null
        val newEntry = Entry(
            id++,
            parseDate(form.date),
            form.text,
            form.respect ?: 0
        )
        val entries = roommate.entries.toMutableList()
        entries.add(newEntry)
        val editedRoommate = Roommate(
            roommate.id,
            roommate.name,
            roommate.location,
            roommate.nightmares,
            entries
        )
        updateRoommate(editedRoommate)
        return newEntry
    }

    fun updateEntry(roommateId: Long, entryId: Long, form: EntryForm): Entry? {
        val roommate = getRoommate(roommateId) ?: return null
        val idx = roommate.entries.indexOfFirst { entry -> entry.id == entryId }
        if (idx < 0) {
            return null
        }
        val updated = Entry(
            entryId,
            parseDate(form.date),
            form.text,
            form.respect ?: 0
        )
        val updatedEntries = roommate.entries.toMutableList()
        updatedEntries.apply { this[idx] = updated }
        updateRoommate(
            Roommate(
                roommate.id,
                roommate.name,
                roommate.location,
                roommate.nightmares,
                updatedEntries
            )
        )
        return updated
    }

    fun deleteEntry(roommateId: Long, entryId: Long): Entry? {
        val roommate = getRoommate(roommateId) ?: return null
        val idx = roommate.entries.indexOfFirst { entry -> entry.id == entryId }
        if (idx < 0) {
            return null
        }
        val updatedEntries = roommate.entries.toMutableList()
        val removed = updatedEntries.removeAt(idx)
        updateRoommate(
            Roommate(
                roommate.id,
                roommate.name,
                roommate.location,
                roommate.nightmares,
                updatedEntries
            )
        )
        return removed
    }

    private fun updateRoommate(roommate: Roommate) {
        val idx = roommates.indexOfFirst { it.id == roommate.id }
        if (idx < 0) {
            return
        }
        roommates.apply { this[idx] = roommate }
    }

    private fun parseDate(date: String?): String {
        if (date == null) {
            return LocalDate.now().toString()
        }
        return try {
            LocalDate.parse(date).toString()
        } catch (e: DateTimeParseException) {
            LocalDate.now().toString()
        }
    }
}