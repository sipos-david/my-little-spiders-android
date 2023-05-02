package dev.shipi.plugins

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.request.path
import io.ktor.server.response.respond

class PluginConfiguration {
    var authHeader: String = "Api-Key"
    var apiKey: String = "123456"
    var exclude: List<String> = listOf()
}

val AuthPlugin =
    createApplicationPlugin(
        name = "AuthPlugin",
        createConfiguration = ::PluginConfiguration
    ) {
        println("INFO  auth - Header is validated.")

        val authHeader = pluginConfig.authHeader
        val apiKey = pluginConfig.apiKey
        val exclude = pluginConfig.exclude

        onCall { call ->
            val headerValue = call.request.headers[authHeader]
            if (!exclude.contains(call.request.path()) &&
                (headerValue == null || headerValue != apiKey)
            ) {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }
    }

fun Application.configureAuthorization(exclude: List<String> = listOf()) {
    install(AuthPlugin) {
        this.exclude = exclude
    }
}