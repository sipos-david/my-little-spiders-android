package dev.shipi.mylittlespiders.data.local

import dev.shipi.mylittlespiders.domain.model.Entry
import dev.shipi.mylittlespiders.domain.model.FriendDetails

interface FriendsDatabase {
    suspend fun getAllFriends(): List<FriendDetails>
    suspend fun getFriendDetails(id: Long): FriendDetails?
    suspend fun addFriend(friend: FriendDetails)
    suspend fun editFriend(friend: FriendDetails)
    suspend fun refreshFriend(friend: FriendDetails)
    suspend fun refreshAllFriends(friends: List<FriendDetails>)
    suspend fun deleteFriend(deleted: FriendDetails)
    suspend fun addEntry(friendId: Long, added: Entry)
    suspend fun editEntry(updated: Entry)
    suspend fun deleteEntry(deleted: Entry)
}