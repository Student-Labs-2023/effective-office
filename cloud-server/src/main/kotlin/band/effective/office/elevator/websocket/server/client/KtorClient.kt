package band.effective.office.elevator.websocket.server.client

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.logging.*

val ktorClient = HttpClient(CIO) {
    defaultRequest {
        //host = "92.124.138.130"
        host = "0.0.0.0"
        //port = 2023
        port = 50
    }
    install(Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.ALL
//        host = "0.0.0.0"
//        port = 50
    }
}