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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking

class FriendsInteractor(
    private val friendsApi: FriendsApi,
    private val friendsDatabase: FriendsDatabase,
) : FriendsRepository {

    private val _friends = MutableStateFlow<List<Friend>>(emptyList())
    override val friends: Flow<List<Friend>> = _friends

    init {
        runBlocking {
            emitList()
        }
    }

    override suspend fun refreshFriends() {
        val network = friendsApi.getAllFriends()
        friendsDatabase.refreshAllFriends(network)
        _friends.emit(network.map { Friend(it.id, it.name, it.location) })
    }

    override suspend fun getFriendDetails(id: Long): FriendDetails? {
        return friendsDatabase.getFriendDetails(id)
    }

    override suspend fun refreshFriendDetails(id: Long): FriendDetails? {
        val network = friendsApi.getFriendDetails(id)
        if (network != null) {
            friendsDatabase.refreshFriend(network)
            emitList()
        }
        return network
    }

    override suspend fun addFriend(new: NewFriend) {
        val added = friendsApi.addFriend(new)
        friendsDatabase.addFriend(added)
        emitList()
    }

    override suspend fun editFriend(edited: EditFriend) {
        val updated = friendsApi.editFriend(edited)
        if (updated != null) {
            friendsDatabase.editFriend(updated)
            emitList()
        }
    }

    override suspend fun deleteFriend(id: Long) {
        val deleted = friendsApi.deleteFriend(id)
        if (deleted != null) {
            friendsDatabase.deleteFriend(deleted)
            emitList()
        }
    }

    override suspend fun addEntry(friendId: Long, new: NewEntry) {
        val added = friendsApi.addEntry(friendId, new)
        if (added != null) {
            friendsDatabase.addEntry(friendId, added)
            emitList()
        }
    }

    override suspend fun editEntry(friendId: Long, edited: Entry) {
        val updated = friendsApi.editEntry(friendId, edited)
        if (updated != null) {
            friendsDatabase.editEntry(updated)
            emitList()
        }
    }

    override suspend fun deleteEntry(friendId: Long, entryId: Long) {
        val deleted = friendsApi.deleteEntry(friendId, entryId)
        if (deleted != null) {
            friendsDatabase.deleteEntry(deleted)
            emitList()
        }
    }

    private suspend fun emitList() {
        _friends.emit(
            friendsDatabase.getAllFriends().map { Friend(it.id, it.name, it.location) })
    }
}