package dev.shipi.mylittlespiders.data.network

import dev.shipi.mylittlespiders.domain.model.EditFriend
import dev.shipi.mylittlespiders.domain.model.Entry
import dev.shipi.mylittlespiders.domain.model.FriendDetails
import dev.shipi.mylittlespiders.domain.model.NewEntry
import dev.shipi.mylittlespiders.domain.model.NewFriend
import java.util.Date

class FriendsApiMock : FriendsApi {
    private var id: Long = 4

    private val friends = mutableListOf(
        FriendDetails(0, "Luigi", "Kitchen", 4, listOf()),
        FriendDetails(1, "D'aand'mo Av'ugd", "cellar", 234, listOf()),
        FriendDetails(
            2, "Quentin Tarantula", "Movie room", 1, listOf(
                Entry(0, Date(), "He suddenly appeared with a movie", 234),
                Entry(1, Date(), "He made a website", 21),
                Entry(
                    2,
                    Date(),
                    "I finally had the courage to ask him how do other spiders find a partner? He said they usually meet on the web!",
                    12
                ),
                Entry(3, Date(), "", 25),
            )
        ),
        FriendDetails(3, "Peter Parker", "Guest room", 0, listOf())
    )

    override fun getAllFriends(): List<FriendDetails> {
        return friends
    }

    override fun getFriendDetails(id: Long): FriendDetails? {
        return friends.firstOrNull {
            it.id == id
        }
    }

    override fun addFriend(new: NewFriend): FriendDetails {
        val friend = FriendDetails(id++, new.name, new.location, new.nightmares, emptyList())
        friends.add(friend)
        return friend
    }

    override fun editFriend(edited: EditFriend): FriendDetails? {
        val friend = getFriendDetails(edited.id) ?: return null
        val updated = FriendDetails(
            friend.id,
            edited.name,
            edited.location,
            edited.nightmares,
            friend.entries
        )
        update(updated)
        return updated
    }

    override fun deleteFriend(id: Long): FriendDetails? {
        val idx = friends.indexOfFirst { it.id == id }
        if (idx < 0) {
            return null
        }
        return friends.removeAt(idx)
    }

    override fun addEntry(friendId: Long, new: NewEntry): Entry? {
        val friend = getFriendDetails(friendId) ?: return null
        val newEntry = Entry(id++, new.date, new.text, new.respect)
        val entries = friend.entries.toMutableList()
        entries.add(newEntry)
        val newFriend = FriendDetails(
            friend.id,
            friend.name,
            friend.location,
            friend.nightmares,
            entries
        )
        update(newFriend)
        return newEntry
    }

    override fun editEntry(edited: Entry): Entry? {
        friends.forEach {
            val idx = it.entries.indexOfFirst { entry -> entry.id == edited.id }
            if (idx > -1) {
                val updatedEntries = it.entries.toMutableList()
                updatedEntries.apply { this[idx] = edited }
                return edited
            }
        }
        return null
    }

    override fun deleteEntry(friendId: Long, entryId: Long): Entry? {
        friends.forEach {
            val idx = it.entries.indexOfFirst { entry -> entry.id == entryId }
            if (idx > -1) {
                val updatedEntries = it.entries.toMutableList()
                return updatedEntries.removeAt(idx)
            }
        }
        return null
    }

    private fun update(friend: FriendDetails) {
        val idx = friends.indexOfFirst { it.id == friend.id }
        if (idx < 0) {
            return
        }
        friends.apply { this[idx] = friend }
    }
}