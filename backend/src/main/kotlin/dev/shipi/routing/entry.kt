package dev.shipi.routing

import dev.shipi.api.EntryForm
import dev.shipi.data.RoommateStorage
import dev.shipi.exceptions.PathException
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.plugins.NotFoundException
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.routing

fun Application.entryRoutes() {
    routing {
        addEntry()
        updateEntry()
        deleteEntry()
    }
}

fun Route.addEntry() {
    post("/roommate/{roommateId}/entries") {
        val id = call.parameters["roommateId"]?.toLong() ?: throw PathException()
        val form = call.receive<EntryForm>()
        val added = RoommateStorage.addEntry(id, form) ?: throw NotFoundException()
        call.respond(added)
    }
}

fun Route.updateEntry() {
    put("/roommate/{roommateId}/entries/{entryId}") {
        val roommateId = call.parameters["roommateId"]?.toLong() ?: throw PathException()
        val entryId = call.parameters["entryId"]?.toLong() ?: throw PathException()
        val form = call.receive<EntryForm>()
        val added =
            RoommateStorage.updateEntry(roommateId, entryId, form) ?: throw NotFoundException()
        call.respond(added)
    }
}

fun Route.deleteEntry() {
    delete("/roommate/{roommateId}/entries/{entryId}") {
        val roommateId = call.parameters["roommateId"]?.toLong() ?: throw PathException()
        val entryId = call.parameters["entryId"]?.toLong() ?: throw PathException()
        val deleted = RoommateStorage.deleteEntry(roommateId, entryId) ?: throw NotFoundException()
        call.respond(deleted)
    }
}
