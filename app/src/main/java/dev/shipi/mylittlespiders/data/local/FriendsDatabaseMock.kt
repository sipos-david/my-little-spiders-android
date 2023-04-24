package dev.shipi.mylittlespiders.data.local

import dev.shipi.mylittlespiders.domain.model.Entry
import dev.shipi.mylittlespiders.domain.model.FriendDetails

class FriendsDatabaseMock : FriendsDatabase {
    private val friends = mutableListOf<FriendDetails>()

    override fun getAllFriends(): List<FriendDetails> {
        return friends
    }

    override fun getFriendDetails(id: Long): FriendDetails? {
        return friends.firstOrNull {
            it.id == id
        }
    }

    override fun save(friend: FriendDetails) {
        update(friend)
    }

    override fun saveAll(friends: List<FriendDetails>) {
        val new = friends.toMutableList()
        this.friends.forEach {
            if (new.isEmpty()) {
                return
            }
            val friend = new.firstOrNull { f -> it.id == f.id }
            if (friend == null) {
                new.add(it)
            } else {
                new.removeAt(new.indexOf(friend))
                update(friend)
            }
        }
        this.friends.addAll(new)
    }

    override fun deleteFriend(deleted: FriendDetails) {
        val idx = friends.indexOfFirst { it.id == deleted.id }
        if (idx < 0) {
            return
        }
        friends.removeAt(idx)
    }

    override fun addEntry(friendId: Long, added: Entry) {
        val friend = getFriendDetails(friendId) ?: return
        val entries = friend.entries.toMutableList()
        entries.add(added)
        val updated = FriendDetails(
            friend.id,
            friend.name,
            friend.location,
            friend.nightmares,
            entries
        )
        update(updated)
    }

    override fun editEntry(updated: Entry) {
        friends.forEach {
            val idx = it.entries.indexOfFirst { entry -> entry.id == updated.id }
            if (idx > -1) {
                val updatedEntries = it.entries.toMutableList()
                updatedEntries.apply { this[idx] = updated }
                update(FriendDetails(it.id, it.name, it.location, it.nightmares, updatedEntries))
                return
            }
        }
    }

    override fun deleteEntry(deleted: Entry) {
        friends.forEach {
            val idx = it.entries.indexOfFirst { entry -> entry.id == deleted.id }
            if (idx > -1) {
                val updatedEntries = it.entries.toMutableList()
                updatedEntries.removeAt(idx)
                update(FriendDetails(it.id, it.name, it.location, it.nightmares, updatedEntries))
                return
            }
        }
    }

    private fun update(friend: FriendDetails) {
        val idx = friends.indexOfFirst { it.id == friend.id }
        if (idx < 0) {
            return
        }
        friends.apply { this[idx] = friend }
        return
    }
}