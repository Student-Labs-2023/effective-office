package band.effective.office.tablet.network.repository

import band.effective.office.tablet.domain.model.Either
import band.effective.office.tablet.domain.model.ErrorWithData
import band.effective.office.tablet.domain.model.RoomInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

interface RoomRepository {
    suspend fun getRoomInfo(): Either<ErrorWithData<RoomInfo>, RoomInfo>
    fun subscribeOnUpdates(scope: CoroutineScope, handler: (Either<ErrorWithData<RoomInfo>, RoomInfo>) -> Unit): Job
}