package dev.shipi.mylittlespiders.domain.repositories

import dev.shipi.mylittlespiders.domain.model.EditFriend
import dev.shipi.mylittlespiders.domain.model.Entry
import dev.shipi.mylittlespiders.domain.model.Friend
import dev.shipi.mylittlespiders.domain.model.FriendDetails
import dev.shipi.mylittlespiders.domain.model.NewEntry
import dev.shipi.mylittlespiders.domain.model.NewFriend

interface FriendsRepository {
    suspend fun getFriends(): List<Friend>
    suspend fun refreshFriends(): List<Friend>
    suspend fun getFriendDetails(id: Long): FriendDetails?
    suspend fun refreshFriendDetails(id: Long): FriendDetails?
    suspend fun addFriend(new: NewFriend)
    suspend fun editFriend(edited: EditFriend)
    suspend fun deleteFriend(id: Long)
    suspend fun addEntry(friendId: Long, new: NewEntry)
    suspend fun editEntry(friendId: Long, edited: Entry)
    suspend fun deleteEntry(friendId: Long, entryId: Long)
}