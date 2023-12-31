package band.effective.office.elevator.domain.useCase

import band.effective.office.elevator.domain.repository.ProfileRepository
import band.effective.office.elevator.domain.models.User

class UpdateUserUseCase (private val profileRepository: ProfileRepository) {
    suspend fun execute(user: User){
        profileRepository.updateUser(User(
            id = user.id,
            imageUrl = user.imageUrl,
            userName = user.userName,
            post = user.post,
            phoneNumber = "+7-"+ user.phoneNumber,
            telegram = "@"+user.telegram,
            email = user.email
        )
        )
    }
}