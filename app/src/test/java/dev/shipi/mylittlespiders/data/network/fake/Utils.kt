package dev.shipi.mylittlespiders.data.network.fake

import dev.shipi.mylittlespiders.data.network.client.infrastructure.Serializer
import kotlinx.serialization.json.encodeToJsonElement
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

inline fun <reified T> success(code: Int, data: T? = null): Response<T> =
    Response.success(
        code, data
    )

inline fun <reified T> error(code: Int, data: T? = null): Response<T> =
    Response.error(
        code, Serializer.kotlinxSerializationJson.encodeToJsonElement(data).toString()
            .toResponseBody("application/json".toMediaTypeOrNull())
    )
