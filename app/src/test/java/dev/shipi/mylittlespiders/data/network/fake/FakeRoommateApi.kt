package dev.shipi.mylittlespiders.data.network.fake

import dev.shipi.mylittlespiders.data.network.client.apis.RoommateApi
import dev.shipi.mylittlespiders.data.network.client.models.Roommate
import dev.shipi.mylittlespiders.data.network.client.models.RoommateForm
import retrofit2.Response

class FakeRoommateApi(private val db: FakeDb) : RoommateApi {
    override suspend fun addRoommate(roommateForm: RoommateForm): Response<Roommate> {
        val created = Roommate(
            id = db.id,
            name = roommateForm.name,
            location = roommateForm.location,
            nightmares = roommateForm.nightmares ?: 0,
            propertyEntries = listOf()
        )
        db.roommates.add(created)
        return success(201, created)
    }

    override suspend fun deleteRoommate(roommateId: Long): Response<Roommate> {
        val index = db.roommates.indexOfFirst { it.id == roommateId }
        if (index < 0) {
            return error(404)
        }
        val roommate = db.roommates.removeAt(index)
        return success(200, roommate)
    }

    override suspend fun findRoommates(): Response<List<Roommate>> {
        return success(200, db.roommates)
    }

    override suspend fun getRoommateById(roommateId: Long): Response<Roommate> {
        val index = db.roommates.indexOfFirst { it.id == roommateId }
        if (index < 0) {
            return error(404)
        }
        val roommate = db.roommates[index]
        return success(200, roommate)
    }

    override suspend fun updateRoommate(
        roommateId: Long,
        roommateForm: RoommateForm
    ): Response<Roommate> {
        val index = db.roommates.indexOfFirst { it.id == roommateId }
        if (index < 0) {
            return error(404)
        }
        val edited = db.roommates[index].copy(
            name = roommateForm.name,
            location = roommateForm.location,
            nightmares = roommateForm.nightmares ?: 0
        )
        db.roommates.apply {
            this[index] = edited
        }
        return success(200, edited)
    }
}