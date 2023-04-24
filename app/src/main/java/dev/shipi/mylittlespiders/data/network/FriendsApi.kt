package dev.shipi.mylittlespiders.data.network

import dev.shipi.mylittlespiders.domain.model.EditFriend
import dev.shipi.mylittlespiders.domain.model.Entry
import dev.shipi.mylittlespiders.domain.model.FriendDetails
import dev.shipi.mylittlespiders.domain.model.NewEntry
import dev.shipi.mylittlespiders.domain.model.NewFriend

interface FriendsApi {
    fun getAllFriends(): List<FriendDetails>
    fun getFriendDetails(id: Long): FriendDetails?
    fun addFriend(new: NewFriend): FriendDetails
    fun editFriend(edited: EditFriend): FriendDetails?
    fun deleteFriend(id: Long): FriendDetails?
    fun addEntry(friendId: Long, new: NewEntry): Entry?
    fun editEntry(edited: Entry): Entry?
    fun deleteEntry(friendId: Long, entryId: Long): Entry?
}