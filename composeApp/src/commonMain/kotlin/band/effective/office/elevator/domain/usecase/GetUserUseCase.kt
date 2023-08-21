package band.effective.office.elevator.domain.useCase

<<<<<<< HEAD
import band.effective.office.elevator.domain.models.ErrorWithData
import band.effective.office.elevator.domain.models.User
import band.effective.office.elevator.domain.repository.ProfileRepository
import band.effective.office.network.model.Either
import kotlinx.coroutines.flow.map

class GetUserUseCase(
    private val profileRepository: ProfileRepository
) {
    suspend fun execute()  = profileRepository.getUser()
    suspend fun executeInFormat() =
        profileRepository.getUser().map{ user ->
            when (user) {
                is Either.Success -> Either.Success(user.data.formatToUI())
                is Either.Error -> Either.Error(
                    ErrorWithData(
                    error = user.error.error,
                    saveData = user.error.saveData?.formatToUI()
                )
                )
            }
        }

    private fun User.formatToUI() =
        User(
            userName = userName,
            id = id,
            imageUrl = imageUrl,
            post = post,
            phoneNumber = phoneNumber.substring(3).replace("-",""),
            telegram = telegram.substring(1),
            email = email
        )
=======
import band.effective.office.elevator.domain.repository.ProfileRepository

class GetUserUseCase(private val userProfileRepository: ProfileRepository) {
    suspend fun execute(idToken: String)  = userProfileRepository.getUser()
>>>>>>> origin/mabileApp/data_layer
}