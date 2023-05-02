package dev.shipi.mylittlespiders.data.network

import dev.shipi.mylittlespiders.data.network.client.apis.EntriesApi
import dev.shipi.mylittlespiders.data.network.client.apis.RoommateApi
import dev.shipi.mylittlespiders.data.network.client.models.EntryForm
import dev.shipi.mylittlespiders.data.network.client.models.Roommate
import dev.shipi.mylittlespiders.data.network.client.models.RoommateForm
import dev.shipi.mylittlespiders.domain.model.EditFriend
import dev.shipi.mylittlespiders.domain.model.Entry
import dev.shipi.mylittlespiders.domain.model.FriendDetails
import dev.shipi.mylittlespiders.domain.model.NewEntry
import dev.shipi.mylittlespiders.domain.model.NewFriend

class FriendsApiNetwork(
    private val roommateApi: RoommateApi,
    private val entriesApi: EntriesApi
) :
    FriendsApi {
    override suspend fun getAllFriends(): List<FriendDetails> {
        val res = roommateApi.findRoommates()
        val body = res.body()
        if (res.isSuccessful && body != null) {
            return body.map { it.toModel() }
        }
        return emptyList()
    }

    override suspend fun getFriendDetails(id: Long): FriendDetails? {
        val res = roommateApi.getRoommateById(id)
        return res.body()?.toModel()
    }

    override suspend fun addFriend(new: NewFriend): FriendDetails {
        val res = roommateApi.addRoommate(new.toNetwork())
        val body = res.body()
        if (!res.isSuccessful || body == null) {
            throw RuntimeException("${res.code()}: ${res.message()}")
        }
        return body.toModel()
    }

    override suspend fun editFriend(edited: EditFriend): FriendDetails? {
        val res = roommateApi.updateRoommate(edited.id, edited.toNetwork())
        if (!res.isSuccessful) {
            throw RuntimeException("${res.code()}: ${res.message()}")
        }
        return res.body()?.toModel()
    }

    override suspend fun deleteFriend(id: Long): FriendDetails? {
        val res = roommateApi.deleteRoommate(id)
        if (!res.isSuccessful) {
            throw RuntimeException("${res.code()}: ${res.message()}")
        }
        return res.body()?.toModel()
    }

    override suspend fun addEntry(friendId: Long, new: NewEntry): Entry? {
        val res = entriesApi.addEntry(friendId, new.toNetwork())
        if (!res.isSuccessful) {
            throw RuntimeException("${res.code()}: ${res.message()}")
        }
        return res.body()?.toModel()
    }

    override suspend fun editEntry(friendId: Long, edited: Entry): Entry? {
        val res = entriesApi.updateEntry(friendId, edited.id, edited.toForm())
        if (!res.isSuccessful) {
            throw RuntimeException("${res.code()}: ${res.message()}")
        }
        return res.body()?.toModel()
    }

    override suspend fun deleteEntry(friendId: Long, entryId: Long): Entry? {
        val res = entriesApi.deleteEntry(friendId, entryId)
        if (!res.isSuccessful) {
            throw RuntimeException("${res.code()}: ${res.message()}")
        }
        return res.body()?.toModel()
    }
}

private fun Entry.toForm() = EntryForm(
    text = this.text,
    date = this.date,
    respect = this.respect.toLong()
)


private fun NewEntry.toNetwork() = EntryForm(
    text = this.text,
    date = this.date,
    respect = this.respect.toLong()
)

private fun EditFriend.toNetwork() = RoommateForm(
    name = this.name,
    location = this.location,
    nightmares = this.nightmares.toLong()
)

private fun NewFriend.toNetwork() = RoommateForm(
    name = this.name,
    location = this.location,
    nightmares = this.nightmares.toLong()
)

private fun Roommate.toModel() = FriendDetails(
    id = this.id!!,
    name = this.name!!,
    location = this.location!!,
    nightmares = this.nightmares!!.toInt(),
    entries = this.propertyEntries!!.map { it.toModel() }
)

private fun dev.shipi.mylittlespiders.data.network.client.models.Entry.toModel() = Entry(
    id = this.id!!,
    date = this.date!!,
    text = this.text!!,
    respect = this.respect!!.toInt()
)