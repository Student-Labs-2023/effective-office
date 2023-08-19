package band.effective.office.elevator.domain.useCase

import band.effective.office.elevator.domain.repository.ProfileRepository
import band.effective.office.elevator.domain.models.User
import band.effective.office.elevator.domain.repository.EmployeeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetUserByIdUseCase(private val employeeRepository: EmployeeRepository) {
    suspend fun execute(id:String) : Flow<User> = employeeRepository.getEmployeeById(id)
    suspend fun executeInFormat(id: String):Flow<User> = flow{
       var userWithoutFormat = User.defaultUser
           profileRepository.getUser().collect{
               user -> userWithoutFormat = user
           }
        emit(User(
            id = userWithoutFormat.id,
            imageUrl = userWithoutFormat.imageUrl,
            userName = userWithoutFormat.userName,
            post = userWithoutFormat.post,
            phoneNumber = userWithoutFormat.phoneNumber.substring(3).replace("-",""),
            telegram = userWithoutFormat.telegram.substring(1),
            email = userWithoutFormat.email
            ))
    }
}