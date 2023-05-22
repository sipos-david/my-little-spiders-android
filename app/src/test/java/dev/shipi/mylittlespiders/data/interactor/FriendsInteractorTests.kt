package dev.shipi.mylittlespiders.data.interactor

import dev.shipi.mylittlespiders.data.FriendsInteractor
import dev.shipi.mylittlespiders.data.interactor.fake.FakeFriendsApi
import dev.shipi.mylittlespiders.data.interactor.fake.FakeFriendsDatabase
import dev.shipi.mylittlespiders.domain.model.EditFriend
import dev.shipi.mylittlespiders.domain.model.Entry
import dev.shipi.mylittlespiders.domain.model.Friend
import dev.shipi.mylittlespiders.domain.model.FriendDetails
import dev.shipi.mylittlespiders.domain.model.NewEntry
import dev.shipi.mylittlespiders.domain.model.NewFriend
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import java.time.LocalDate


@OptIn(ExperimentalCoroutinesApi::class)
class FriendsInteractorTests {
    private lateinit var fakeFriendsApi: FakeFriendsApi
    private lateinit var fakeFriendsDatabase: FakeFriendsDatabase

    @Before
    fun initDb() {
        fakeFriendsApi = FakeFriendsApi()
        fakeFriendsDatabase = FakeFriendsDatabase()
    }

    @Test
    fun test_initialEmit() = runTest {
        // Arrange
        val alice = FriendDetails(0, "alice", "kitchen", 2, listOf())
        val bob = FriendDetails(1, "bob", "bedroom", 4, listOf())
        fakeFriendsDatabase.addFriend(alice)
        fakeFriendsDatabase.addFriend(bob)

        // Act
        val friendsInteractor = FriendsInteractor(fakeFriendsApi, fakeFriendsDatabase)

        // Assert
        val actual = friendsInteractor.friends.first()
        assertEquals(2, actual.size)
        val networkAlice = actual.firstOrNull { it.name == alice.name }
        assertEquals(alice, networkAlice)
        val networkBob = actual.firstOrNull { it.name == bob.name }
        assertEquals(bob, networkBob)

        val db = fakeFriendsDatabase.getAllFriends()
        assertEquals(2, db.size)
        val dbAlice = db.firstOrNull { it.name == alice.name }
        assertEquals(alice, dbAlice)
        val dbBob = db.firstOrNull { it.name == bob.name }
        assertEquals(bob, dbBob)
    }

    @Test
    fun test_refreshFriends() = runTest {
        // Arrange
        val alice = fakeFriendsApi.addFriend(NewFriend("alice", "bedroom", 0))
        val bob = fakeFriendsApi.addFriend(NewFriend("bob", "kitchen", 1))

        // Act
        val friendsInteractor = FriendsInteractor(fakeFriendsApi, fakeFriendsDatabase)
        friendsInteractor.refreshFriends()

        // Assert
        val actual = friendsInteractor.friends.first()
        assertEquals(2, actual.size)
        val networkAlice = actual.firstOrNull { it.name == alice.name }
        assertEquals(alice, networkAlice)
        val networkBob = actual.firstOrNull { it.name == bob.name }
        assertEquals(bob, networkBob)
    }

    @Test
    fun test_getFriendDetails() = runTest {
        // Arrange
        val alice = FriendDetails(0, "alice", "kitchen", 2, listOf())
        val bob = FriendDetails(1, "bob", "bedroom", 4, listOf())
        fakeFriendsDatabase.addFriend(alice)
        fakeFriendsDatabase.addFriend(bob)

        // Act
        val friendsInteractor = FriendsInteractor(fakeFriendsApi, fakeFriendsDatabase)

        // Assert
        val actual = friendsInteractor.getFriendDetails(alice.id)
        assertEquals(alice, actual)
    }

    @Test
    fun test_refreshFriendDetails() = runTest {
        // Arrange
        val alice = FriendDetails(0, "alice", "kitchen", 2, listOf())
        val bob = FriendDetails(1, "bob", "bedroom", 4, listOf())
        fakeFriendsDatabase.addFriend(alice)
        fakeFriendsDatabase.addFriend(bob)
        val changedAlice = NewFriend(
            alice.name + " test",
            alice.location + " test",
            alice.nightmares + 1
        )
        fakeFriendsApi.addFriend(changedAlice)

        // Act
        val friendsInteractor = FriendsInteractor(fakeFriendsApi, fakeFriendsDatabase)
        val refreshedAlice = friendsInteractor.refreshFriendDetails(alice.id)

        // Assert
        assertNotNull(refreshedAlice)
        assertEquals(changedAlice.name, refreshedAlice!!.name)
        assertEquals(changedAlice.location, refreshedAlice.location)
        assertEquals(changedAlice.nightmares, refreshedAlice.nightmares)
    }

    @Test
    fun test_addFriend() = runTest {
        // Arrange
        val alice = NewFriend("alice", "kitchen", 1)

        // Act
        val friendsInteractor = FriendsInteractor(fakeFriendsApi, fakeFriendsDatabase)
        friendsInteractor.addFriend(alice)

        // Assert
        val actual = friendsInteractor.friends.first()
        assertEquals(1, actual.size)
        val actualAlice = actual.first()
        assertEquals(alice.name, actualAlice.name)
        assertEquals(alice.location, actualAlice.location)

        val db = fakeFriendsDatabase.getAllFriends()
        assertEquals(1, db.size)
        val dbAlice = db.first()
        assertEquals(alice.name, dbAlice.name)
        assertEquals(alice.location, dbAlice.location)
        assertEquals(alice.nightmares, dbAlice.nightmares)

        val api = fakeFriendsApi.getAllFriends()
        assertEquals(1, api.size)
        val apiAlice = db.first()
        assertEquals(alice.name, apiAlice.name)
        assertEquals(alice.location, apiAlice.location)
        assertEquals(alice.nightmares, apiAlice.nightmares)
    }

    @Test
    fun test_editFriend() = runTest {
        // Arrange
        val apiAlice = NewFriend("alice", "kitchen", 1)
        val dbAlice =
            FriendDetails(0, apiAlice.name, apiAlice.location, apiAlice.nightmares, listOf())
        fakeFriendsDatabase.addFriend(dbAlice)
        fakeFriendsApi.addFriend(apiAlice)

        val changes = EditFriend(
            dbAlice.id,
            dbAlice.name + " test",
            dbAlice.location + " test",
            dbAlice.nightmares + 1
        )

        // Act
        val friendsInteractor = FriendsInteractor(fakeFriendsApi, fakeFriendsDatabase)
        friendsInteractor.editFriend(changes)

        // Assert
        val actual = friendsInteractor.friends.first()
        assertEquals(1, actual.size)
        val actualAlice = actual.first()
        assertEquals(changes.name, actualAlice.name)
        assertEquals(changes.location, actualAlice.location)

        val db = fakeFriendsDatabase.getAllFriends()
        assertEquals(1, db.size)
        val updatedDbAlice = db.first()
        assertEquals(changes.name, updatedDbAlice.name)
        assertEquals(changes.location, updatedDbAlice.location)
        assertEquals(changes.nightmares, updatedDbAlice.nightmares)

        val api = fakeFriendsApi.getAllFriends()
        assertEquals(1, api.size)
        val updatedApiAlice = db.first()
        assertEquals(changes.name, updatedApiAlice.name)
        assertEquals(changes.location, updatedApiAlice.location)
        assertEquals(changes.nightmares, updatedApiAlice.nightmares)
    }

    @Test
    fun test_deleteFriend() = runTest {
        // Arrange
        val apiAlice = NewFriend("alice", "kitchen", 1)
        val dbAlice =
            FriendDetails(0, apiAlice.name, apiAlice.location, apiAlice.nightmares, listOf())
        fakeFriendsDatabase.addFriend(dbAlice)
        fakeFriendsApi.addFriend(apiAlice)

        // Act
        val friendsInteractor = FriendsInteractor(fakeFriendsApi, fakeFriendsDatabase)
        friendsInteractor.deleteFriend(dbAlice.id)

        // Assert
        val actual = friendsInteractor.friends.first()
        assertEquals(0, actual.size)

        val db = fakeFriendsDatabase.getAllFriends()
        assertEquals(0, db.size)
        val api = fakeFriendsApi.getAllFriends()
        assertEquals(0, api.size)
    }

    @Test
    fun test_addEntry() = runTest {
        // Arrange
        val apiAlice = NewFriend("alice", "kitchen", 1)
        val dbAlice =
            FriendDetails(0, apiAlice.name, apiAlice.location, apiAlice.nightmares, listOf())
        fakeFriendsDatabase.addFriend(dbAlice)
        fakeFriendsApi.addFriend(apiAlice)

        val newEntry = NewEntry(LocalDate.now(), "Entry text", 2)

        // Act
        val friendsInteractor = FriendsInteractor(fakeFriendsApi, fakeFriendsDatabase)
        friendsInteractor.addEntry(dbAlice.id, newEntry)

        // Assert
        val actual = friendsInteractor.friends.first()
        assertEquals(1, actual.size)

        val db = fakeFriendsDatabase.getAllFriends()
        assertEquals(1, db.size)
        val updatedDbAlice = db.first()
        assertEquals(1, updatedDbAlice.entries.size)
        val dbEntry = updatedDbAlice.entries.first()
        assertEquals(newEntry.date, dbEntry.date)
        assertEquals(newEntry.text, dbEntry.text)
        assertEquals(newEntry.respect, dbEntry.respect)

        val api = fakeFriendsApi.getAllFriends()
        assertEquals(1, api.size)
        val apiEntry = api.first().entries.first()
        assertEquals(newEntry.date, apiEntry.date)
        assertEquals(newEntry.text, apiEntry.text)
        assertEquals(newEntry.respect, apiEntry.respect)
    }

    @Test
    fun test_editEntry() = runTest {
        // Arrange
        val apiAlice = NewFriend("alice", "kitchen", 1)
        val apiEntry = dev.shipi.mylittlespiders.data.network.client.models.Entry(
            1,
            LocalDate.now(),
            "Entry",
            1
        )
        val dbAlice =
            FriendDetails(
                0, apiAlice.name, apiAlice.location, apiAlice.nightmares, listOf(
                    Entry(
                        apiEntry.id!!,
                        apiEntry.date!!,
                        apiEntry.text!!,
                        apiEntry.respect!!.toInt()
                    )
                )
            )
        fakeFriendsDatabase.addFriend(dbAlice)
        fakeFriendsApi.addFriend(apiAlice)
        fakeFriendsApi.addEntry(
            dbAlice.id,
            NewEntry(apiEntry.date!!, apiEntry.text!!, apiEntry.respect!!.toInt())
        )

        val changedEntry = Entry(
            apiEntry.id!!,
            apiEntry.date!!.plusDays(1),
            apiEntry.text!! + " test",
            apiEntry.respect!!.toInt() + 1
        )

        // Act
        val friendsInteractor = FriendsInteractor(fakeFriendsApi, fakeFriendsDatabase)
        friendsInteractor.editEntry(dbAlice.id, changedEntry)

        // Assert
        val actual = friendsInteractor.friends.first()
        assertEquals(1, actual.size)

        val db = fakeFriendsDatabase.getAllFriends()
        assertEquals(1, db.size)
        val updatedDbAlice = db.first()
        assertEquals(1, updatedDbAlice.entries.size)
        val changedDbEntry = updatedDbAlice.entries.first()
        assertEquals(changedEntry.date, changedDbEntry.date)
        assertEquals(changedEntry.text, changedDbEntry.text)
        assertEquals(changedEntry.respect, changedDbEntry.respect)

        val api = fakeFriendsApi.getAllFriends()
        assertEquals(1, api.size)
        val changedApiEntry = api.first().entries.first()
        assertEquals(changedEntry.date, changedApiEntry.date)
        assertEquals(changedEntry.text, changedApiEntry.text)
        assertEquals(changedEntry.respect, changedApiEntry.respect)
    }

    @Test
    fun test_deleteEntry() = runTest {
        // Arrange
        val apiAlice = NewFriend("alice", "kitchen", 1)
        val apiEntry = dev.shipi.mylittlespiders.data.network.client.models.Entry(
            1,
            LocalDate.now(),
            "Entry",
            1
        )
        val dbAlice =
            FriendDetails(
                0, apiAlice.name, apiAlice.location, apiAlice.nightmares, listOf(
                    Entry(
                        apiEntry.id!!,
                        apiEntry.date!!,
                        apiEntry.text!!,
                        apiEntry.respect!!.toInt()
                    )
                )
            )
        fakeFriendsDatabase.addFriend(dbAlice)
        fakeFriendsApi.addFriend(apiAlice)
        fakeFriendsApi.addEntry(
            dbAlice.id,
            NewEntry(apiEntry.date!!, apiEntry.text!!, apiEntry.respect!!.toInt())
        )

        // Act
        val friendsInteractor = FriendsInteractor(fakeFriendsApi, fakeFriendsDatabase)
        friendsInteractor.deleteEntry(dbAlice.id, apiEntry.id!!)

        // Assert
        val actual = friendsInteractor.friends.first()
        assertEquals(1, actual.size)

        val db = fakeFriendsDatabase.getAllFriends()
        assertEquals(1, db.size)
        assertEquals(0, db.first().entries.size)

        val api = fakeFriendsApi.getAllFriends()
        assertEquals(1, api.size)
        assertEquals(0, api.first().entries.size)
    }

    // Helper functions

    private fun assertEquals(expected: FriendDetails, actual: Friend?) {
        assertNotNull(actual)
        assertEquals(expected.location, actual!!.location)
    }
}