package dev.shipi.mylittlespiders.data

import dev.shipi.mylittlespiders.data.local.FriendsDatabase
import dev.shipi.mylittlespiders.data.network.FriendsApi
import dev.shipi.mylittlespiders.domain.model.EditFriend
import dev.shipi.mylittlespiders.domain.model.Entry
import dev.shipi.mylittlespiders.domain.model.Friend
import dev.shipi.mylittlespiders.domain.model.FriendDetails
import dev.shipi.mylittlespiders.domain.model.NewEntry
import dev.shipi.mylittlespiders.domain.model.NewFriend
import dev.shipi.mylittlespiders.domain.repositories.FriendsRepository

class FriendsInteractor(
    private val friendsApi: FriendsApi,
    private val friendsDatabase: FriendsDatabase
) : FriendsRepository {

    override suspend fun getFriends(): List<Friend> {
        return friendsDatabase.getAllFriends().map { Friend(it.id, it.name, it.location) }
    }

    override suspend fun refreshFriends(): List<Friend> {
        val network = friendsApi.getAllFriends()
        friendsDatabase.saveAll(network)
        return network.map { Friend(it.id, it.name, it.location) }
    }

    override suspend fun getFriendDetails(id: Long): FriendDetails? {
        return friendsDatabase.getFriendDetails(id)
    }

    override suspend fun refreshFriendDetails(id: Long): FriendDetails? {
        val network = friendsApi.getFriendDetails(id)
        if (network != null) {
            friendsDatabase.save(network)
        }
        return network
    }

    override suspend fun addFriend(new: NewFriend) {
        val added = friendsApi.addFriend(new)
        friendsDatabase.save(added)
    }

    override suspend fun editFriend(edited: EditFriend) {
        val updated = friendsApi.editFriend(edited)
        if (updated != null) {
            friendsDatabase.save(updated)
        }
    }

    override suspend fun deleteFriend(id: Long) {
        val deleted = friendsApi.deleteFriend(id)
        if (deleted != null) {
            friendsDatabase.deleteFriend(deleted)
        }
    }

    override suspend fun addEntry(friendId: Long, new: NewEntry) {
        val added = friendsApi.addEntry(friendId, new)
        if (added != null) {
            friendsDatabase.addEntry(friendId, added)
        }
    }

    override suspend fun editEntry(edited: Entry) {
        val updated = friendsApi.editEntry(edited)
        if (updated != null) {
            friendsDatabase.editEntry(updated)
        }
    }

    override suspend fun deleteEntry(friendId: Long, entryId: Long) {
        val deleted = friendsApi.deleteEntry(friendId, entryId)
        if (deleted != null) {
            friendsDatabase.deleteEntry(deleted)
        }
    }
}