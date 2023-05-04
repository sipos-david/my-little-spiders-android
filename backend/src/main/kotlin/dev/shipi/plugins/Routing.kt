package dev.shipi.plugins

import dev.shipi.exceptions.PathException
import dev.shipi.routing.entryRoutes
import dev.shipi.routing.roommateRoutes
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.ContentTransformationException
import io.ktor.server.plugins.NotFoundException
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respondText

fun Application.configureRouting() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            when (cause) {
                is ContentTransformationException, is PathException, is BadRequestException -> {
                    call.respondText(text = "400: $cause", status = HttpStatusCode.BadRequest)
                }

                is NotFoundException -> {
                    call.respondText(text = "404: $cause", status = HttpStatusCode.NotFound)
                }

                else -> {
                    call.respondText(
                        text = "500: $cause",
                        status = HttpStatusCode.InternalServerError
                    )
                }
            }
        }
    }
    roommateRoutes()
    entryRoutes()
}
