package band.effective.office.tablet.network.repository.impl

import band.effective.office.tablet.domain.model.Either
import band.effective.office.tablet.domain.model.EventInfo
import band.effective.office.tablet.network.api.Api
import band.effective.office.tablet.network.repository.BookingRepository
import network.model.ErrorResponse

class BookingRepositoryImpl(private val api: Api) : BookingRepository {
    override suspend fun bookingRoom(eventInfo: EventInfo): Either<ErrorResponse, String> = api.bookingRoom(
        begin = eventInfo.startTime,
        end = eventInfo.finishTime,
        owner = eventInfo.organizer
    )
}