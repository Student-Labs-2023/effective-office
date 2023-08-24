package office.effective.features.calendar.service

import com.google.api.client.util.DateTime
import com.google.api.services.calendar.model.Event
import com.google.api.services.calendar.model.Event.Organizer
import com.google.api.services.calendar.model.EventAttendee
import com.google.api.services.calendar.model.EventDateTime
import model.Recurrence.Companion.toRecurrence
import office.effective.common.utils.UuidValidator
import office.effective.config
import office.effective.dto.BookingDTO
import office.effective.dto.UserDTO
import office.effective.dto.WorkspaceDTO
import office.effective.features.booking.converters.BookingFacadeConverter
import office.effective.features.calendar.repository.CalendarRepository
import office.effective.features.user.converters.UserDTOModelConverter
import office.effective.features.user.repository.UserRepository
import office.effective.features.workspace.converters.WorkspaceFacadeConverter
import office.effective.model.Booking
import org.koin.core.context.GlobalContext
import utils.RecurrenceRuleFactory.getRecurrence
import utils.RecurrenceRuleFactory.rule

object GoogleCalendarConverter {

    private val defaultAccount: String =
        config.propertyOrNull("auth.app.defaultAppEmail")?.getString() ?: throw Exception(
            "Config file does not contain default gmail value"
        )

    fun Event.toBookingDTO(): BookingDTO {
        val email = if (organizer.email != defaultAccount) organizer.email else description.substringAfter(" ")
        val recurrence = recurrence?.toString()?.getRecurrence()?.toDto()
        return BookingDTO(
            owner = getUser(email),
            participants = attendees.filter { !it.resource }.map { getUser(it.email) },
            workspace = getWorkspace(attendees.first { it.resource }.email),
            id = null,
            beginBooking = start.dateTime.value,
            endBooking = end.dateTime.value,
            recurrence = recurrence
        )
    }

    private fun getWorkspace(calendarId: String): WorkspaceDTO {
        // достаём воркспейс по гугловкому id
        val calendarRepository: CalendarRepository = GlobalContext.get().get()
        val workspaceModel = calendarRepository.findWorkspaceById(calendarId)
        val workspaceConverter: WorkspaceFacadeConverter = GlobalContext.get().get()
        return workspaceConverter.modelToDto(workspaceModel)
    }

    private fun getUser(email: String): UserDTO {
        val userRepository: UserRepository = GlobalContext.get().get()
        val userModel = userRepository.findByEmail(email)
        val converter: UserDTOModelConverter = GlobalContext.get().get()
        return converter.modelToDTO(userModel)
    }

    fun BookingDTO.toGoogleEvent(): Event {
        val dto = this
        return Event().apply {
            summary = "${dto.owner.fullName}: create from office application"
            description =
                "${dto.owner.email} - почта организатора"//"${dto.owner.integrations?.first { it.name == "email" }} - почта организатора"
            organizer = owner.toGoogleOrganizer()
            attendees = participants.map { it.toAttendee() } + owner.toAttendee()
                .apply { organizer = true } + workspace.toAttendee()
            if (dto.recurrence != null) recurrence = listOf(dto.recurrence.toRecurrence().rule())
            start = beginBooking.toGoogleDateTime()
            end = endBooking.toGoogleDateTime()
        }
    }

    fun Booking.toGoogleEvent(): Event {
        val converter: BookingFacadeConverter = GlobalContext.get().get()
        return converter.modelToDto(this).toGoogleEvent()
    }

    fun Event.toBookingModel(): Booking {
        val converter: BookingFacadeConverter = GlobalContext.get().get()
        return converter.dtoToModel(this.toBookingDTO());
    }

    private fun Long.toGoogleDateTime(): EventDateTime? {
        return EventDateTime().also {
            it.dateTime = DateTime(this)
            it.timeZone = "Asia/Omsk"
        }
    }

    private fun UserDTO.toAttendee(): EventAttendee {
        return EventAttendee().also {
            it.email =
                this.email// this.integrations?.first { it.name == "email" }?.value //TODO надо допилить получение почты
        }
    }

    private fun UserDTO.toGoogleOrganizer(): Event.Organizer? {
        return Organizer().also {
            it.email =
                this.email//this.integrations?.first { integ -> integ.name == "email" }?.value //TODO надо допилить получение почты
        }
    }

    private fun WorkspaceDTO.toAttendee(): EventAttendee {
        return EventAttendee().also {
            it.email = getCalendarIdById(id) //TODO надо допилить получение комнаты
            it.resource = true
        }
    }

    val calendarRepository: CalendarRepository = GlobalContext.get().get()

    private fun getCalendarIdById(id: String): String {
        val verifier = UuidValidator()
        return calendarRepository.findByWorkspace(verifier.uuidFromString(id))
    }
}