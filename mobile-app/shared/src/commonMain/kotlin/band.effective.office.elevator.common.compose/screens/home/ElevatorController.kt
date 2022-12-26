package band.effective.office.elevator.common.compose.screens.home

import band.effective.office.elevator.common.compose.helpers.BaseUrl
import band.effective.office.elevator.common.compose.screens.login.GoogleAuthorization
import io.github.aakira.napier.Napier
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

object ElevatorController {

    private val client: HttpClient = HttpClient() {
        defaultRequest {
            host = BaseUrl
            port = 80
        }
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Napier.i(message, null, "HTTP Client")
                }
            }
        }
    }

    suspend fun callElevator(): Result<HttpResponse> {
        return runCatching {
            client.get("elevate") {
                parameter("key", GoogleAuthorization.token)
            }
        }
    }
}