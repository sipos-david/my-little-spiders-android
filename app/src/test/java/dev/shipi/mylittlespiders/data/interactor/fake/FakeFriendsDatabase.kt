package dev.shipi.mylittlespiders.data.interactor.fake

import dev.shipi.mylittlespiders.data.local.FriendsDatabase
import dev.shipi.mylittlespiders.domain.model.Entry
import dev.shipi.mylittlespiders.domain.model.FriendDetails

class FakeFriendsDatabase : FriendsDatabase {
    private val friends = mutableListOf<FriendDetails>()

    override suspend fun getAllFriends(): List<FriendDetails> {
        return friends
    }

    override suspend fun getFriendDetails(id: Long): FriendDetails? {
        return friends.firstOrNull {
            it.id == id
        }
    }

    override suspend fun addFriend(friend: FriendDetails) {
        friends.add(friend)
    }

    override suspend fun refreshAllFriends(friends: List<FriendDetails>) {
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
                editFriend(friend)
            }
        }
        this.friends.addAll(new)
    }

    override suspend fun deleteFriend(deleted: FriendDetails) {
        val idx = friends.indexOfFirst { it.id == deleted.id }
        if (idx < 0) {
            return
        }
        friends.removeAt(idx)
    }

    override suspend fun addEntry(friendId: Long, added: Entry) {
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
        editFriend(updated)
    }

    override suspend fun editEntry(updated: Entry) {
        friends.forEach {
            val idx = it.entries.indexOfFirst { entry -> entry.id == updated.id }
            if (idx > -1) {
                val updatedEntries = it.entries.toMutableList()
                updatedEntries.apply { this[idx] = updated }
                editFriend(
                    FriendDetails(
                        it.id,
                        it.name,
                        it.location,
                        it.nightmares,
                        updatedEntries
                    )
                )
                return
            }
        }
    }

    override suspend fun deleteEntry(deleted: Entry) {
        friends.forEach {
            val idx = it.entries.indexOfFirst { entry -> entry.id == deleted.id }
            if (idx > -1) {
                val updatedEntries = it.entries.toMutableList()
                updatedEntries.removeAt(idx)
                editFriend(
                    FriendDetails(
                        it.id,
                        it.name,
                        it.location,
                        it.nightmares,
                        updatedEntries
                    )
                )
                return
            }
        }
    }

    override suspend fun editFriend(friend: FriendDetails) {
        val idx = friends.indexOfFirst { it.id == friend.id }
        if (idx < 0) {
            return
        }
        friends.apply { this[idx] = friend }
        return
    }

    override suspend fun refreshFriend(friend: FriendDetails) {
        val existing = friends.firstOrNull { it.id == friend.id }
        if (existing != null) {
            return editFriend(friend)
        }
        refreshFriend(friend)
    }
}