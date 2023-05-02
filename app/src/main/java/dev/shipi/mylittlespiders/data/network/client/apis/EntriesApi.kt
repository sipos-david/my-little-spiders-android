package dev.shipi.mylittlespiders.data.network.client.apis

import dev.shipi.mylittlespiders.data.network.client.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

import dev.shipi.mylittlespiders.data.network.client.models.Entry
import dev.shipi.mylittlespiders.data.network.client.models.EntryForm

interface EntriesApi {
    /**
     * Add new entry to a roommate
     * Add new entry to a roommate
     * Responses:
     *  - 201: successful operation
     *  - 400: Invalid roommateId supplied
     *  - 401: Invalid api key
     *  - 404: Roommate not found
     *
     * @param roommateId ID of roommate
     * @param entryForm  (optional)
     * @return [Entry]
     */
    @POST("roommate/{roommateId}/entries")
    suspend fun addEntry(@Path("roommateId") roommateId: kotlin.Long, @Body entryForm: EntryForm? = null): Response<Entry>

    /**
     * Deletes an entry from a roommate
     * Delete an entry
     * Responses:
     *  - 200: successful operation
     *  - 400: Invalid roommateId or entryId supplied
     *  - 401: Invalid api key
     *  - 404: Roommate or entry not found
     *
     * @param roommateId Id of the roommate which contains the entry
     * @param entryId ID of entry to delete
     * @return [Entry]
     */
    @DELETE("roommate/{roommateId}/entries/{entryId}")
    suspend fun deleteEntry(@Path("roommateId") roommateId: kotlin.Long, @Path("entryId") entryId: kotlin.Long): Response<Entry>

    /**
     * Updates an entry for a roommate
     * Updates an entry
     * Responses:
     *  - 200: successful operation
     *  - 400: Invalid roommateId or entryId supplied
     *  - 401: Invalid api key
     *  - 404: Roommate or entry not found
     *
     * @param roommateId Id of the roommate which contains the entry
     * @param entryId ID of entry to change
     * @param entryForm  (optional)
     * @return [Entry]
     */
    @PUT("roommate/{roommateId}/entries/{entryId}")
    suspend fun updateEntry(@Path("roommateId") roommateId: kotlin.Long, @Path("entryId") entryId: kotlin.Long, @Body entryForm: EntryForm? = null): Response<Entry>

}
