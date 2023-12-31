package band.effective.office.elevator.cloud.server

import band.effective.office.elevator.cloud.server.plugins.configureRouting
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 80, host = "0.0.0.0") {
        configureRouting()
    }.start(wait = true)
}
