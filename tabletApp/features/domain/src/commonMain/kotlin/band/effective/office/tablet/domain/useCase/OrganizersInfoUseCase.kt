package band.effective.office.tablet.domain.useCase

import band.effective.office.tablet.network.repository.OrganizerRepository
import kotlinx.coroutines.CoroutineScope

/**Use case for get organizers list*/
class OrganizersInfoUseCase(private val repository: OrganizerRepository) {
    suspend operator fun invoke() = repository.getOrganizersList()

    /**Subscribe on changes information
     * @param scope scope for collect new information
     * @param handler handler for new information*/
    fun subscribe(scope: CoroutineScope) = repository.subscribeOnUpdates(scope)
}