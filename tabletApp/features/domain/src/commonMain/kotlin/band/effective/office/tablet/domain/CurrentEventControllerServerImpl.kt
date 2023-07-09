package band.effective.office.tablet.domain

import band.effective.office.tablet.domain.useCase.RoomInfoUseCase
import band.effective.office.tablet.network.repository.CancelRepository
import band.effective.office.tablet.network.repository.ServerUpdateRepository
import kotlinx.coroutines.launch

class CurrentEventControllerServerImpl(
    private val roomUseCase: RoomInfoUseCase,
    private val serverUpdateRepository: ServerUpdateRepository,
    private val cancelRepository: CancelRepository
) : CurrentEventController(roomUseCase, serverUpdateRepository, cancelRepository) {
    override fun update() = scope.launch {}
}