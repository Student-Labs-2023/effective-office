import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

import office.effective.features.user.ITokenVerifier
import office.effective.features.user.converters.UserDTOModelConverter
import office.effective.features.user.dto.UserDTO
import office.effective.features.user.repository.UserRepository
import office.effective.features.user.service.IUserService
import org.koin.core.context.GlobalContext

fun Route.authRoutingFun() {

    val service: IUserService = GlobalContext.get().get()
    val verifier: ITokenVerifier = GlobalContext.get().get()

    authenticate("auth-oauth-google") {
        get("/login") {

        }
        get("/callback") {
            try {
//
                val principal: OAuthAccessTokenResponse.OAuth2? = call.principal()
                call.respondText(verifier.isCorrectToken(principal!!.extraParameters["id_token"] ?: ""))

            } catch (ex: Exception) {

                var trace: String = ex.message ?: "There are no message.\n";
                ex.stackTrace.forEach { trace += it.toString() + "\n" }
                trace += "\n"
                trace += ex.cause
                call.respond(trace)

            }
            call.respondRedirect("http://localhost:8080/callback")
        }
    }

    get("/usersTest/{email}") {
        val repo: UserRepository = GlobalContext.get().get()
        val model =
            repo.findByEmail((call.parameters["email"] ?: call.response.status(HttpStatusCode.BadRequest)) as String)
        val converterDTO = UserDTOModelConverter()
        call.respond(converterDTO.modelToDTO(model))
    }
    get("/users") {
        var tagStr = call.request.queryParameters["tag"] ?: call.response.status(HttpStatusCode.BadRequest)
        val tokenStr = call.request.header("id_token") ?: call.response.status(HttpStatusCode.Forbidden)
        val users: Set<UserDTO>? = service.getUsersByTag(tagStr as String, tokenStr as String)
        call.respond(users ?: "no such users")
    }
    get("/users/{user_id}") {
        val userId = call.parameters["user_id"] ?: call.response.status(HttpStatusCode.BadRequest)
        val tokenStr = call.request.header("id_token") ?: call.response.status(HttpStatusCode.Forbidden)
        val user = service.getUserById(userId as String, tokenStr as String)
        call.respond(user ?: "No such user. Suggestion: bad id")
    }

}
