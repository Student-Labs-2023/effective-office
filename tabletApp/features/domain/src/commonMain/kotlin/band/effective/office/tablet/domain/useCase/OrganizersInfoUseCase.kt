package band.effective.office.tablet.domain.useCase

import band.effective.office.tablet.domain.model.Either
import band.effective.office.tablet.domain.model.ErrorWithData
import band.effective.office.tablet.network.repository.OrganizerRepository
import kotlinx.coroutines.CoroutineScope

/**Use case for get organizers list*/
class OrganizersInfoUseCase(private val repository: OrganizerRepository) {
    suspend operator fun invoke() = repository.getOrganizersList()

    /**Subscribe on changes information
     * @param scope scope for collect new information
     * @param handler handler for new information*/
    fun subscribe(scope: CoroutineScope, handler: (Either<ErrorWithData<List<String>>, List<String>>) -> Unit) {
        repository.subscribeOnUpdates(scope) { handler(it) }
    }
}