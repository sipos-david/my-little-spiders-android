package dev.shipi.mylittlespiders.data.local

import dev.shipi.mylittlespiders.domain.model.Entry
import dev.shipi.mylittlespiders.domain.model.FriendDetails

interface FriendsDatabase {
    fun getAllFriends(): List<FriendDetails>
    fun getFriendDetails(id: Long): FriendDetails?
    fun save(friend: FriendDetails)
    fun saveAll(friends: List<FriendDetails>)
    fun deleteFriend(deleted: FriendDetails)
    fun addEntry(friendId: Long, added: Entry)
    fun editEntry(updated: Entry)
    fun deleteEntry(deleted: Entry)
}