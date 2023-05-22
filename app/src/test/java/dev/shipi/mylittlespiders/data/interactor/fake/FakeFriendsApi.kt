package dev.shipi.mylittlespiders.data.interactor.fake

import dev.shipi.mylittlespiders.data.network.FriendsApi
import dev.shipi.mylittlespiders.domain.model.EditFriend
import dev.shipi.mylittlespiders.domain.model.Entry
import dev.shipi.mylittlespiders.domain.model.FriendDetails
import dev.shipi.mylittlespiders.domain.model.NewEntry
import dev.shipi.mylittlespiders.domain.model.NewFriend

class FakeFriendsApi : FriendsApi {
    private var id: Long = 0

    private val friends = mutableListOf<FriendDetails>()

    override suspend fun getAllFriends(): List<FriendDetails> {
        return friends
    }

    override suspend fun getFriendDetails(id: Long): FriendDetails? {
        return friends.firstOrNull {
            it.id == id
        }
    }

    override suspend fun addFriend(new: NewFriend): FriendDetails {
        val friend = FriendDetails(id++, new.name, new.location, new.nightmares, emptyList())
        friends.add(friend)
        return friend
    }

    override suspend fun editFriend(edited: EditFriend): FriendDetails? {
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

    override suspend fun deleteFriend(id: Long): FriendDetails? {
        val idx = friends.indexOfFirst { it.id == id }
        if (idx < 0) {
            return null
        }
        return friends.removeAt(idx)
    }

    override suspend fun addEntry(friendId: Long, new: NewEntry): Entry? {
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

    override suspend fun editEntry(friendId: Long, edited: Entry): Entry? {
        val friend = friends.firstOrNull { it.id == friendId } ?: return null
        val idx = friend.entries.indexOfFirst { entry -> entry.id == edited.id }
        if (idx < 0) {
            return null
        }
        val updatedEntries = friend.entries.toMutableList()
        updatedEntries.apply { this[idx] = edited }
        update(
            FriendDetails(
                friend.id,
                friend.name,
                friend.location,
                friend.nightmares,
                updatedEntries
            )
        )
        return edited
    }

    override suspend fun deleteEntry(friendId: Long, entryId: Long): Entry? {
        friends.forEach {
            val idx = it.entries.indexOfFirst { entry -> entry.id == entryId }
            if (idx > -1) {
                val updatedEntries = it.entries.toMutableList()
                val removed = updatedEntries.removeAt(idx)
                update(FriendDetails(it.id, it.name, it.location, it.nightmares, updatedEntries))
                return removed
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