package office.effective.features.booking.converters

import office.effective.common.utils.UuidValidator
import office.effective.features.booking.dto.BookingDTO
import office.effective.features.user.converters.UserDTOModelConverter
import office.effective.features.workspace.converters.WorkspaceFacadeConverter
import office.effective.model.Booking
import java.time.Instant

class BookingFacadeConverter(private val userConverter: UserDTOModelConverter,
                             private val workspaceConverter: WorkspaceFacadeConverter,
                             private val uuidValidator: UuidValidator) {
    fun modelToDto(booking: Booking): BookingDTO {
        return BookingDTO(
            owner = userConverter.modelToDTO(booking.owner),
            participants = booking.participants.map { userConverter.modelToDTO(it) },
            workspace = workspaceConverter.modelToDto(booking.workspace),
            id = booking.id.toString(),
            beginBooking = booking.beginBooking.epochSecond,
            endBooking = booking.endBooking.epochSecond
        )
    }

    fun dtoToModel(bookingDTO: BookingDTO): Booking {
        return Booking(
            owner = userConverter.dTOToModel(bookingDTO.owner),
            participants = bookingDTO.participants.map { userConverter.dTOToModel(it) },
            workspace = workspaceConverter.dtoToModel(bookingDTO.workspace),
            id = bookingDTO.id?.let { uuidValidator.uuidFromString(it) },
            beginBooking = Instant.ofEpochSecond(bookingDTO.beginBooking),
            endBooking = Instant.ofEpochSecond(bookingDTO.endBooking)
        )
    }
}