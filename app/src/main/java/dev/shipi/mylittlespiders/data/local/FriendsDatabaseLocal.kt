package dev.shipi.mylittlespiders.data.local

import dev.shipi.mylittlespiders.data.local.dao.EntryDao
import dev.shipi.mylittlespiders.data.local.dao.RoommateDao
import dev.shipi.mylittlespiders.data.local.model.Roommate
import dev.shipi.mylittlespiders.data.local.model.RoommateWithEntries
import dev.shipi.mylittlespiders.domain.model.Entry
import dev.shipi.mylittlespiders.domain.model.FriendDetails

class FriendsDatabaseLocal(
    private val roommateDao: RoommateDao,
    private val entryDao: EntryDao,
) : FriendsDatabase {
    override suspend fun getAllFriends(): List<FriendDetails> =
        roommateDao.getRoommatesWithEntries().map { it.toModel() }

    override suspend fun getFriendDetails(id: Long): FriendDetails? =
        roommateDao.getRoommateDetails(id)?.toModel()

    override suspend fun addFriend(friend: FriendDetails) {
        refreshFriend(friend)
    }

    override suspend fun editFriend(friend: FriendDetails) {
        roommateDao.update(friend.toEntity())
    }

    override suspend fun refreshFriend(friend: FriendDetails) {
        roommateDao.insert(friend.toEntity())
        entryDao.deleteByRoommate(friend.id)
        entryDao.insertAll(friend.entries.map { it.toEntity(friend.id) })
    }

    override suspend fun refreshAllFriends(friends: List<FriendDetails>) {
        val deletedRoommates = roommateDao.getAllRoommatesNotIn(friends.map { it.id })
        roommateDao.deleteAllById(deletedRoommates)
        entryDao.deleteAllByRoommateIds(deletedRoommates)
        entryDao.deleteAllEntryNotIn(friends.flatMap { it.entries.map { e -> e.id } })
        roommateDao.insertAll(friends.map { it.toEntity() })
        entryDao.insertAll(friends.flatMap { it.entries.map { e -> e.toEntity(it.id) } })
    }

    override suspend fun deleteFriend(deleted: FriendDetails) {
        entryDao.deleteAllById(deleted.entries.map { it.id })
        roommateDao.delete(deleted.toEntity())
    }

    override suspend fun addEntry(friendId: Long, added: Entry) {
        entryDao.insert(added.toEntity(friendId))
    }

    override suspend fun editEntry(updated: Entry) {
        entryDao.update(updated.id, updated.date, updated.text, updated.respect.toLong())
    }

    override suspend fun deleteEntry(deleted: Entry) {
        entryDao.deleteById(deleted.id)
    }
}

private fun Entry.toEntity(roommateId: Long) = dev.shipi.mylittlespiders.data.local.model.Entry(
    id = this.id,
    date = this.date,
    text = this.text,
    respect = this.respect.toLong(),
    roommateId = roommateId
)

private fun FriendDetails.toEntity() = Roommate(
    id = this.id,
    name = this.name,
    location = this.location,
    nightmares = this.nightmares
)

private fun RoommateWithEntries.toModel() = FriendDetails(id = this.roommate.id,
    name = this.roommate.name,
    location = this.roommate.location,
    nightmares = this.roommate.nightmares,
    entries = this.entries.map { it.toModel() })

private fun dev.shipi.mylittlespiders.data.local.model.Entry.toModel() = Entry(
    id = this.id, date = this.date, text = this.text, respect = this.respect.toInt()
)
