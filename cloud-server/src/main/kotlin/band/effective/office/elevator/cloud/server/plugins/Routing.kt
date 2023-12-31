package band.effective.office.elevator.cloud.server.plugins

import band.effective.office.common.utils.HashUtil
import band.effective.office.common.utils.PropertiesUtil
import band.effective.office.common.utils.toVerifiableDate
import band.effective.office.elevator.cloud.server.client.ktorClient
import band.effective.office.elevator.cloud.server.utils.TokenVerifier
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.date.*

fun Application.configureRouting() {
    routing {

        get("/") {
            call.respond("Server API")
        }

        get("/elevate") {
            val key = call.request.queryParameters["key"]
            if (TokenVerifier.isCorrectToken(key)) {
                val request = ktorClient.post {
                    url {
                        path("office-elevator")
                        parameters.append(
                            "command", "go"
                        )

                        val currentTime = GMTDate()
                        parameters.append(
                            "time", currentTime.toHttpDate()
                        )
                        parameters.append(
                            "token",
                            HashUtil.sha256(
                                value = currentTime.toVerifiableDate(),
                                password = PropertiesUtil.read("OFFICE_ELEVATOR_EXCHANGE_PASSWORD")
                            )
                        )
                    }
                }
                when (request.status.value) {
                    in 200..299 -> {
                        call.respond(HttpStatusCode.OK, "Success")
                    }

                    404 -> {
                        call.respond(HttpStatusCode.NotFound, "Not found")
                    }

                    403 -> {
                        call.respond(HttpStatusCode.Forbidden, request.status.description)
                    }

                    500 -> {
                        call.respond(HttpStatusCode.InternalServerError, "Internal server error")
                    }
                }
                call.respond(HttpStatusCode.OK, "Success")
            } else {
                call.respond(
                    HttpStatusCode.Forbidden, "Google token verification is failed",
                )
            }
        }
    }
}