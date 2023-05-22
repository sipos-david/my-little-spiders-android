package dev.shipi.mylittlespiders.data.network


import dev.shipi.mylittlespiders.data.network.client.models.Entry
import dev.shipi.mylittlespiders.data.network.client.models.Roommate
import dev.shipi.mylittlespiders.data.network.fake.FakeDb
import dev.shipi.mylittlespiders.data.network.fake.FakeEntriesApi
import dev.shipi.mylittlespiders.data.network.fake.FakeRoommateApi
import dev.shipi.mylittlespiders.domain.model.EditFriend
import dev.shipi.mylittlespiders.domain.model.FriendDetails
import dev.shipi.mylittlespiders.domain.model.NewEntry
import dev.shipi.mylittlespiders.domain.model.NewFriend
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

class FriendsApiNetworkTests {
    // Fake dependencies
    private val db = FakeDb()
    private val fakeEntriesApi = FakeEntriesApi(db)
    private val fakeRoommateApi = FakeRoommateApi(db)

    // SUT
    private val friendsApiNetwork = FriendsApiNetwork(fakeRoommateApi, fakeEntriesApi)

    @Before
    fun initDb() {
        db.clear()
    }

    @Test
    fun test_getFriendDetails() = runBlocking {
        // Arrange
        val alice = createAlice()
        val bob = createBob()
        db.roommates.add(alice)
        db.roommates.add(bob)

        // Act
        val actualAlice = friendsApiNetwork.getFriendDetails(alice.id!!)

        // Assert
        assertNotNull(actualAlice)
        assertEquals(alice, actualAlice!!)
    }

    @Test
    fun test_getAllFriends() = runBlocking {
        // Arrange
        val alice = createAlice()
        val bob = createBob()
        db.roommates.add(alice)
        db.roommates.add(bob)

        // Act
        val friends = friendsApiNetwork.getAllFriends()

        // Assert
        assertEquals(2, friends.size)
        val actualAlice = friends.firstOrNull { it.id == alice.id }
        assertNotNull(actualAlice)
        assertEquals(alice, actualAlice!!)
    }

    @Test
    fun test_addFriend() = runBlocking {
        // Arrange
        val alice = createAlice()
        val new = NewFriend(alice.name!!, alice.location!!, alice.nightmares!!.toInt())

        // Act
        val added = friendsApiNetwork.addFriend(new)

        // Assert
        assertNotNull(added)
        assertEquals(alice, added)
    }

    @Test
    fun test_editFriend() = runBlocking {
        // Arrange
        val alice = createAlice()

        db.roommates.add(alice)
        val changes = EditFriend(
            alice.id!!,
            alice.name!! + " test",
            alice.location!! + " test",
            alice.nightmares!!.toInt() + 1
        )

        // Act
        val edited = friendsApiNetwork.editFriend(changes)

        // Assert
        assertNotNull(edited)
        assertEquals(alice.id, edited!!.id)
        assertEquals(changes.name, edited.name)
        assertEquals(changes.location, edited.location)
        assertEquals(changes.nightmares, edited.nightmares)
        assertEquals(alice.propertyEntries?.size ?: 0, edited.entries.size)
    }

    @Test
    fun test_deleteFriend() = runBlocking {
        // Arrange
        val alice = createAlice()
        val bob = createBob()
        db.roommates.add(alice)
        db.roommates.add(bob)

        // Act
        val deleted = friendsApiNetwork.deleteFriend(alice.id!!)

        // Assert
        assertNotNull(deleted)
        assertEquals(alice.id, deleted!!.id)
        assertEquals(alice.name, deleted.name)
        assertEquals(alice.location, deleted.location)
        assertEquals(alice.nightmares, deleted.nightmares.toLong())
        assertEquals(alice.propertyEntries?.size ?: 0, deleted.entries.size)

        assertEquals(1, db.roommates.size)
        assertEquals(bob, db.roommates[0])
    }

    @Test
    fun test_addEntry() = runBlocking {
        // Arrange
        val alice = createAlice()
        val bob = createBob()
        db.roommates.add(alice)
        db.roommates.add(bob)
        val entry = createEntry(alice.id!!)
        val new = NewEntry(entry.date!!, entry.text!!, entry.respect!!.toInt())

        // Act
        val actual = friendsApiNetwork.addEntry(alice.id!!, new)

        // Assert
        assertNotNull(actual)
        assertEquals(entry.date, actual!!.date)
        assertEquals(entry.text, actual.text)
        assertEquals(entry.respect!!.toInt(), actual.respect)
        val aliceDb = db.roommates.first { it.id == alice.id }
        assertEquals(1, aliceDb.propertyEntries!!.size)
        val bobDb = db.roommates.first { it.id == bob.id }
        assertEquals(0, bobDb.propertyEntries!!.size)
    }

    @Test
    fun test_editEntry() = runBlocking {
        // Arrange
        val entry = createEntry(0)
        val alice = createAlice(listOf(entry))
        val bob = createBob()
        db.roommates.add(alice)
        db.roommates.add(bob)
        val changes = dev.shipi.mylittlespiders.domain.model.Entry(
            entry.id!!,
            entry.date!!.plusDays(1),
            entry.text!! + " test",
            entry.respect!!.toInt() + 1
        )

        // Act
        val actual = friendsApiNetwork.editEntry(alice.id!!, changes)

        // Assert
        assertNotNull(actual)
        assertEquals(changes.date, actual!!.date)
        assertEquals(changes.text, actual.text)
        assertEquals(changes.respect, actual.respect)
        val aliceDb = db.roommates.first { it.id == alice.id }
        assertEquals(1, aliceDb.propertyEntries!!.size)
        val bobDb = db.roommates.first { it.id == bob.id }
        assertEquals(0, bobDb.propertyEntries!!.size)
    }

    @Test
    fun test_deleteEntry() = runBlocking {
        // Arrange
        val entry = createEntry(0)
        val alice = createAlice(listOf(entry))
        val bob = createBob()
        db.roommates.add(alice)
        db.roommates.add(bob)

        // Act
        val actual = friendsApiNetwork.deleteEntry(alice.id!!, entry.id!!)

        // Assert
        assertNotNull(actual)
        assertEquals(entry.date, actual!!.date)
        assertEquals(entry.text, actual.text)
        assertEquals(entry.respect!!.toInt(), actual.respect)
        val aliceDb = db.roommates.first { it.id == alice.id }
        assertEquals(0, aliceDb.propertyEntries!!.size)
        val bobDb = db.roommates.first { it.id == bob.id }
        assertEquals(0, bobDb.propertyEntries!!.size)
    }

    // Helper functions

    private fun createAlice(entries: List<Entry> = listOf()) =
        Roommate(
            id = 0,
            name = "Alice",
            nightmares = 1,
            location = "Bedroom",
            propertyEntries = entries
        )

    private fun createBob(entries: List<Entry> = listOf()) = Roommate(
        id = 1,
        name = "Bob",
        nightmares = 2,
        location = "Kitchen",
        propertyEntries = entries
    )

    private fun createEntry(id: Long = 0) = Entry(id, LocalDate.now(), "We had beer", 0)

    private fun assertEquals(expected: Roommate, actual: FriendDetails) {
        assertEquals(expected.id, actual.id)
        assertEquals(expected.name, actual.name)
        assertEquals(expected.location, actual.location)
        assertEquals(expected.nightmares, actual.nightmares.toLong())
        assertEquals(expected.propertyEntries?.size ?: 0, actual.entries.size)
    }
}