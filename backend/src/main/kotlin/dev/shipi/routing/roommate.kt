package dev.shipi.routing

import dev.shipi.api.RoommateForm
import dev.shipi.data.RoommateStorage
import dev.shipi.exceptions.PathException

import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.plugins.NotFoundException
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.routing

fun Application.roommateRoutes() {
    routing {
        findRoommates()
        addRoommate()
        getRoommateById()
        updateRoommate()
        deleteRoommate()
    }
}

fun Route.findRoommates() {
    get("/roommate") {
        call.respond(RoommateStorage.getAllRoommates())
    }
}

fun Route.addRoommate() {
    post("/roommate") {
        val form = call.receive<RoommateForm>()
        val roommate = RoommateStorage.addRoommate(form)
        call.respond(roommate)
    }
}

fun Route.getRoommateById() {
    get("/roommate/{roommateId}") {
        val id = call.parameters["roommateId"]?.toLong() ?: throw PathException()
        val roommate = RoommateStorage.getRoommate(id) ?: throw NotFoundException()
        call.respond(roommate)
    }
}

fun Route.updateRoommate() {
    put("/roommate/{roommateId}") {
        val id = call.parameters["roommateId"]?.toLong() ?: throw PathException()
        val form = call.receive<RoommateForm>()
        val updated = RoommateStorage.updateRoommate(id, form) ?: throw NotFoundException()
        call.respond(updated)
    }
}

fun Route.deleteRoommate() {
    delete("/roommate/{roommateId}") {
        val id = call.parameters["roommateId"]?.toLong() ?: throw PathException()
        val deleted = RoommateStorage.deleteRoommate(id) ?: throw NotFoundException()
        call.respond(deleted)
    }
}
