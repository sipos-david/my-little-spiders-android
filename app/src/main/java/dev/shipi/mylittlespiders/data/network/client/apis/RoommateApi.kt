package dev.shipi.mylittlespiders.data.network.client.apis

import dev.shipi.mylittlespiders.data.network.client.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

import dev.shipi.mylittlespiders.data.network.client.models.Roommate
import dev.shipi.mylittlespiders.data.network.client.models.RoommateForm

interface RoommateApi {
    /**
     * Add a new roommate
     * Add a new roommate to the list
     * Responses:
     *  - 201: Successful operation
     *  - 400: Invalid form
     *  - 401: Invalid api key
     *
     * @param roommateForm Create a new roommate
     * @return [Roommate]
     */
    @POST("roommate")
    suspend fun addRoommate(@Body roommateForm: RoommateForm): Response<Roommate>

    /**
     * Deletes a roommate
     * Delete a roommate
     * Responses:
     *  - 200: successful operation
     *  - 400: Invalid roommateId supplied
     *  - 401: Invalid api key
     *  - 404: Roommate not found
     *
     * @param roommateId Roommate id to delete
     * @return [Unit]
     */
    @DELETE("roommate/{roommateId}")
    suspend fun deleteRoommate(@Path("roommateId") roommateId: kotlin.Long): Response<Unit>

    /**
     * Find all roommates
     * Get all available roommates
     * Responses:
     *  - 200: successful operation
     *  - 401: Invalid api key
     *
     * @return [kotlin.collections.List<Roommate>]
     */
    @GET("roommate")
    suspend fun findRoommates(): Response<kotlin.collections.List<Roommate>>

    /**
     * Find roommate by Id
     * Returns a single roommate
     * Responses:
     *  - 200: successful operation
     *  - 400: Invalid ID supplied
     *  - 401: Invalid api key
     *  - 404: Roommate not found
     *
     * @param roommateId ID of roommate to return
     * @return [Roommate]
     */
    @GET("roommate/{roommateId}")
    suspend fun getRoommateById(@Path("roommateId") roommateId: kotlin.Long): Response<Roommate>

    /**
     * Update a roommate
     * Updates a roommate with form data
     * Responses:
     *  - 200: successful operation
     *  - 400: Invalid roommateId supplied or invalid form
     *  - 401: Invalid api key
     *  - 404: Roommate not found
     *
     * @param roommateId ID of roommate that needs to be updated
     * @param roommateForm Roommate edited data
     * @return [Unit]
     */
    @PUT("roommate/{roommateId}")
    suspend fun updateRoommate(@Path("roommateId") roommateId: kotlin.Long, @Body roommateForm: RoommateForm): Response<Unit>

}
