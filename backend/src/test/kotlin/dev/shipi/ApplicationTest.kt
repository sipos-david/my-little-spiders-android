package dev.shipi

import dev.shipi.data.RoommateStorage
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.server.testing.*
import kotlin.test.*
import io.ktor.http.*
import dev.shipi.plugins.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ApplicationTest {
    @Test
    fun test_GET_Roommate() = testApplication {
        application {
            configureSerialization()
            configureRouting()
        }
        client.get("/roommate").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals(Json.encodeToString(RoommateStorage.getAllRoommates()), bodyAsText())
        }
    }
}
