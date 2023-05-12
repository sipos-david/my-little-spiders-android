package dev.shipi.mylittlespiders.data.local

import dev.shipi.mylittlespiders.data.local.fake.FakeDb
import dev.shipi.mylittlespiders.data.local.fake.FakeEntryDao
import dev.shipi.mylittlespiders.data.local.fake.FakeRoommateDao
import dev.shipi.mylittlespiders.data.local.model.Entry
import dev.shipi.mylittlespiders.data.local.model.Roommate
import dev.shipi.mylittlespiders.domain.model.FriendDetails
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

class FriendsDatabaseLocalTests {
    // Fake dependencies
    private val db = FakeDb()
    private val fakeEntryDao = FakeEntryDao(db)
    private val fakeRoommateDao = FakeRoommateDao(db)

    // SUT
    private val friendsDatabaseLocal = FriendsDatabaseLocal(fakeRoommateDao, fakeEntryDao)

    @Before
    fun initDb() {
        db.clear()
    }

    @Test
    fun test_getAllFriends() = runBlocking {
        // Arrange
        val friend1 = createAlice()
        val entry1 = createEntry(roommateId = friend1.id)
        val friend2 = createBob()
        db.entries.add(entry1)
        db.roommates.add(friend1)
        db.roommates.add(friend2)

        // Act
        val friends = friendsDatabaseLocal.getAllFriends()

        // Assert
        assertEquals(2, friends.size)

        val actualFriend1 = friends.firstOrNull { it.id == friend1.id }
        assertNotNull(actualFriend1)
        assertEquals(friend1, 1, actualFriend1!!)

        val actualEntry1 = actualFriend1.entries.first()
        assertEquals(entry1, actualEntry1)

        val actualFriend2 = friends.firstOrNull { it.id == friend2.id }
        assertNotNull(actualFriend2)
        assertEquals(friend2, 0, actualFriend2!!)
    }

    @Test
    fun test_getFriendDetails_bad_id() = runBlocking {
        // Arrange
        val friend1 = createAlice()
        val entry1 = createEntry(roommateId = friend1.id)
        val friend2 = createBob()
        db.entries.add(entry1)
        db.roommates.add(friend1)
        db.roommates.add(friend2)

        // Act
        val friend = friendsDatabaseLocal.getFriendDetails(69)

        // Assert
        assertNull(friend)
    }

    @Test
    fun test_getFriendDetails_good_id() = runBlocking {
        // Arrange
        val friend1 = createAlice()
        val entry1 = createEntry(roommateId = friend1.id)
        val friend2 = createBob()
        db.entries.add(entry1)
        db.roommates.add(friend1)
        db.roommates.add(friend2)

        // Act
        val friend = friendsDatabaseLocal.getFriendDetails(friend1.id)

        // Assert
        assertNotNull(friend)
        assertEquals(friend1, 1, friend!!)
    }

    @Test
    fun test_addFriend() = runBlocking {
        // Arrange
        val friend = createAlice()
        val entry = createEntry(roommateId = friend.id)
        val details = createDetails(friend, listOf(entry))

        // Act
        friendsDatabaseLocal.addFriend(details)

        // Assert
        assertEquals(1, db.roommates.size)
        assertEquals(1, db.entries.size)

        val actualFriend = db.roommates.first()
        assertEquals(friend, actualFriend)

        val actualEntry = db.entries.first()
        assertEquals(entry, actualEntry)
    }

    @Test
    fun test_updateFriend() = runBlocking {
        // Arrange
        val friend1 = createAlice()
        val friend2 = Roommate(
            id = friend1.id,
            name = friend1.name + " test",
            nightmares = friend1.nightmares + 1,
            location = friend1.location + " test"
        )
        db.roommates.add(friend1)
        val details = createDetails(friend2, listOf())

        // Act
        friendsDatabaseLocal.editFriend(details)

        // Assert
        val actualFriend = db.roommates.first()
        assertEquals(friend2, actualFriend)
    }

    @Test
    fun test_refreshFriend() = runBlocking {
        // Arrange
        val friend1 = createAlice()
        val friend2 = Roommate(
            id = friend1.id,
            name = friend1.name + " test",
            nightmares = friend1.nightmares + 1,
            location = friend1.location + " test"
        )
        val entry = createEntry(roommateId = friend1.id)
        val details = createDetails(friend2, listOf(entry))
        db.roommates.add(friend1)

        // Act
        friendsDatabaseLocal.refreshFriend(details)

        // Assert
        assertEquals(1, db.roommates.size)
        assertEquals(1, db.entries.size)

        val actualFriend = db.roommates.first()
        assertEquals(friend2, actualFriend)

        val actualEntry = db.entries.first()
        assertEquals(entry, actualEntry)
    }

    @Test
    fun test_refreshFriends() = runBlocking {
        // Arrange
        val friend1 = createAlice()
        val friend1edited = Roommate(
            id = friend1.id,
            name = friend1.name + " test",
            nightmares = friend1.nightmares + 1,
            location = friend1.location + " test"
        )
        val friend2 = createBob()
        val entry1 = createEntry(roommateId = friend1.id)
        val entry2 = createEntry(1, friend2.id)
        db.roommates.add(friend1)
        db.entries.add(entry1)

        val entry1edited = Entry(entry1.id, friend2.id, entry1.date, entry1.text, entry1.respect)
        val details1 = createDetails(friend1edited, listOf())
        val details2 = createDetails(friend2, listOf(entry1edited, entry2))

        // Act
        friendsDatabaseLocal.refreshAllFriends(listOf(details1, details2))

        // Assert
        assertEquals(2, db.roommates.size)
        assertEquals(2, db.entries.size)

        val actualFriend1 = db.roommates.firstOrNull { it.id == friend1.id }
        assertNotNull(actualFriend1)
        assertEquals(friend1edited, actualFriend1)

        val actualFriend2 = db.roommates.firstOrNull { it.id == friend2.id }
        assertNotNull(actualFriend2)
        assertEquals(friend2, actualFriend2)

        val actualEntry1 = db.entries.firstOrNull { it.id == entry1.id }
        assertNotNull(actualEntry1)
        assertEquals(entry1edited, actualEntry1)

        val actualEntry2 = db.entries.firstOrNull { it.id == entry2.id }
        assertNotNull(actualEntry2)
        assertEquals(entry2, actualEntry2)
    }

    @Test
    fun test_deleteFriend() = runBlocking {
        // Arrange
        val friend1 = createAlice()
        val entry1 = createEntry(roommateId = friend1.id)
        val friend2 = createBob()
        db.entries.add(entry1)
        db.roommates.add(friend1)
        db.roommates.add(friend2)

        val details = createDetails(friend1, listOf(entry1))

        // Act
        friendsDatabaseLocal.deleteFriend(details)

        // Assert
        assertEquals(1, db.roommates.size)
        assertEquals(0, db.entries.size)

        val actualFriend = db.roommates.first()
        assertEquals(friend2, actualFriend)
    }

    @Test
    fun test_addEntry() = runBlocking {
        // Arrange
        val friend = createAlice()
        val entry = createEntry(roommateId = friend.id)
        db.roommates.add(friend)
        val model = dev.shipi.mylittlespiders.domain.model.Entry(
            entry.id,
            entry.date,
            entry.text,
            entry.respect.toInt()
        )
        // Act
        friendsDatabaseLocal.addEntry(friend.id,model)

        // Assert
        assertEquals(1, db.roommates.size)
        assertEquals(1, db.entries.size)

        val actualEntry = db.entries.first()
        assertEquals(entry, actualEntry)
    }

    @Test
    fun test_editEntry() = runBlocking {
        // Arrange
        val friend = createAlice()
        val entry = createEntry(roommateId = friend.id)
        db.roommates.add(friend)
        db.entries.add(entry)
        val model = dev.shipi.mylittlespiders.domain.model.Entry(
            entry.id,
            entry.date.plusDays(1),
            entry.text + " test",
            entry.respect.toInt() + 1
        )
        // Act
        friendsDatabaseLocal.editEntry(model)

        // Assert
        assertEquals(1, db.roommates.size)
        assertEquals(1, db.entries.size)

        val actualEntry = db.entries.first()
        assertEquals(entry.id, actualEntry.id)
        assertEquals(model.date, actualEntry.date)
        assertEquals(model.text, actualEntry.text)
        assertEquals(model.respect.toLong(), actualEntry.respect)
    }

    @Test
    fun test_deleteEntry() = runBlocking {
        // Arrange
        val friend = createAlice()
        val entry1 = createEntry(roommateId = friend.id)
        val entry2 = createEntry(roommateId = friend.id, entryId = 1)
        db.roommates.add(friend)
        db.entries.add(entry1)
        db.entries.add(entry2)
        val model = dev.shipi.mylittlespiders.domain.model.Entry(
            entry1.id,
            entry1.date,
            entry1.text,
            entry1.respect.toInt()
        )
        // Act
        friendsDatabaseLocal.deleteEntry(model)

        // Assert
        assertEquals(1, db.roommates.size)
        assertEquals(1, db.entries.size)

        val actualEntry = db.entries.first()
        assertEquals(entry2, actualEntry)
    }

    // Helper functions

    private fun assertEquals(
        expected: Entry,
        actual: dev.shipi.mylittlespiders.domain.model.Entry
    ) {
        assertEquals(expected.date, actual.date)
        assertEquals(expected.text, actual.text)
        assertEquals(expected.respect.toInt(), actual.respect)
    }

    private fun assertEquals(
        expected: Roommate,
        expectedEntries: Int,
        actual: FriendDetails
    ) {
        assertEquals(expected.name, actual.name)
        assertEquals(expected.location, actual.location)
        assertEquals(expected.nightmares, actual.nightmares)
        assertEquals(expectedEntries, actual.entries.size)
    }

    private fun createDetails(
        friend: Roommate,
        entries: List<Entry>
    ) = FriendDetails(
        id = friend.id,
        name = friend.name,
        location = friend.location,
        nightmares = friend.nightmares,
        entries = entries.map {
            dev.shipi.mylittlespiders.domain.model.Entry(
                id = it.id,
                date = it.date,
                text = it.text,
                respect = it.respect.toInt()
            )
        }
    )

    private fun createAlice() =
        Roommate(id = 0, name = "Alice", nightmares = 1, location = "Bedroom")

    private fun createBob() = Roommate(id = 1, name = "Bob", nightmares = 2, location = "Kitchen")
    private fun createEntry(entryId: Long = 0, roommateId: Long = 0) = Entry(
        id = entryId,
        roommateId = roommateId,
        date = LocalDate.now(),
        text = "Alice said hi!",
        respect = 2
    )
}