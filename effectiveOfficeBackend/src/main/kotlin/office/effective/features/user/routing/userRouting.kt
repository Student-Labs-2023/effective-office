package office.effective.features.user.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import office.effective.features.user.ITokenVerifier
import office.effective.features.user.converters.UserDTOModelConverter
import office.effective.features.user.dto.UserDTO
import office.effective.features.user.repository.UserRepository
import office.effective.features.user.service.IUserService
import org.koin.core.context.GlobalContext

fun Route.userRouting() {
    val service: IUserService = GlobalContext.get().get()
    val verifier: ITokenVerifier = GlobalContext.get().get()

    get("/usersTest/{email}") {
        val repo: UserRepository = GlobalContext.get().get()
        val model =
            repo.findByEmail((call.parameters["email"] ?: return@get call.respond(HttpStatusCode.BadRequest)))
        val converterDTO = UserDTOModelConverter()
        call.respond(converterDTO.modelToDTO(model))
    }
    get("/users") {
        var tagStr = call.request.queryParameters["tag"] ?: return@get call.respond(HttpStatusCode.BadRequest)
        val tokenStr = call.request.header("id_token") ?: return@get call.respond(HttpStatusCode.Forbidden)
        val users: Set<UserDTO>? = service.getUsersByTag(tagStr, tokenStr)
        call.respond(users ?: "no such users")
    }
    get("/users/{user_id}") {
        val userId = call.parameters["user_id"] ?: call.response.status(HttpStatusCode.BadRequest)
        val tokenStr = call.request.header("id_token") ?: return@get call.respond(HttpStatusCode.Forbidden)
        val user = service.getUserById(userId as String, tokenStr)
        call.respond(user)
    }
}