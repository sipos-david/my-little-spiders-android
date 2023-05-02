package dev.shipi.mylittlespiders.data.network

import dev.shipi.mylittlespiders.domain.model.EditFriend
import dev.shipi.mylittlespiders.domain.model.Entry
import dev.shipi.mylittlespiders.domain.model.FriendDetails
import dev.shipi.mylittlespiders.domain.model.NewEntry
import dev.shipi.mylittlespiders.domain.model.NewFriend

interface FriendsApi {
    suspend fun getAllFriends(): List<FriendDetails>
    suspend fun getFriendDetails(id: Long): FriendDetails?
    suspend fun addFriend(new: NewFriend): FriendDetails
    suspend fun editFriend(edited: EditFriend): FriendDetails?
    suspend fun deleteFriend(id: Long): FriendDetails?
    suspend fun addEntry(friendId: Long, new: NewEntry): Entry?
    suspend fun editEntry(friendId: Long, edited: Entry): Entry?
    suspend fun deleteEntry(friendId: Long, entryId: Long): Entry?
}