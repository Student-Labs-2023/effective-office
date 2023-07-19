package band.effective.office.tablet.domain.useCase

import band.effective.office.tablet.domain.model.EventInfo
import band.effective.office.tablet.network.repository.BookingRepository

class BookingUseCase(private val repository: BookingRepository) {
    suspend operator fun invoke(eventInfo: EventInfo) = repository.bookingRoom(eventInfo)
}