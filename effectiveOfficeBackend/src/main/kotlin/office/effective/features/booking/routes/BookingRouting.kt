package office.effective.features.booking.routes

import io.github.smiley4.ktorswaggerui.dsl.delete
import io.github.smiley4.ktorswaggerui.dsl.get
import io.github.smiley4.ktorswaggerui.dsl.post
import io.github.smiley4.ktorswaggerui.dsl.put
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import office.effective.common.notifications.INotificationSender
import office.effective.common.swagger.SwaggerDocument
import office.effective.dto.BookingDTO
import office.effective.features.booking.facade.BookingFacade
import office.effective.features.booking.routes.swagger.*
import org.koin.core.context.GlobalContext

fun Route.bookingRouting() {
    route("/bookings") {
        val bookingFacade = BookingFacade(
            GlobalContext.get().get(),
            GlobalContext.get().get(),
            GlobalContext.get().get(),
            GlobalContext.get().get()
        )
        val sender: INotificationSender = GlobalContext.get().get()

        get("/{id}", SwaggerDocument.returnBookingById()) {
            val id: String = call.parameters["id"]
                ?: return@get call.respond(HttpStatusCode.BadRequest)
            call.respond(bookingFacade.findById(id))
        }

        get(SwaggerDocument.returnBookings()) {
            val userId: String? = call.request.queryParameters["user_id"]
            val workspaceId: String? = call.request.queryParameters["workspace_id"]
            call.respond(bookingFacade.findAll(userId, workspaceId))
        }
        post(SwaggerDocument.postBooking()) {
            val dto = call.receive<BookingDTO>()

            call.response.status(HttpStatusCode.Created)
            val result = bookingFacade.post(dto)
            sender.sendEmptyMessage("booking")
            call.respond(result)
        }
        put(SwaggerDocument.putBooking()) {
            val dto = call.receive<BookingDTO>()

            val result = bookingFacade.put(dto)
            sender.sendEmptyMessage("booking")
            call.respond(result)
        }
        delete("{id}", SwaggerDocument.deleteBookingById()) {
            val id: String = call.parameters["id"]
                ?: return@delete call.respond(HttpStatusCode.BadRequest)

            bookingFacade.deleteById(id)
            sender.sendEmptyMessage("booking")
            call.respond(HttpStatusCode.NoContent)
        }
    }
}