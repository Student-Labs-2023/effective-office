package band.effective.office.elevator.ui.authorization.authorization_google

import band.effective.office.elevator.domain.GoogleSignIn
import band.effective.office.elevator.domain.models.UserData
import band.effective.office.elevator.domain.usecase.phone_authorization.GetUserUseCase
import band.effective.office.elevator.ui.authorization.authorization_google.store.AuthorizationGoogleStore
import band.effective.office.elevator.ui.authorization.authorization_google.store.AuthorizationGoogleStoreFactory
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AuthorizationGoogleComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    private val signInClient: GoogleSignIn,
    private val output: (Output) -> Unit
) : ComponentContext by componentContext, KoinComponent {

    private val useCase: GetUserUseCase by inject()

    private val authorizationStore =
        instanceKeeper.getStore {
            AuthorizationGoogleStoreFactory(
                storeFactory = storeFactory,
                signInClient = signInClient,
                getUserUseCase = useCase
            ).create()
        }

    val label: Flow<AuthorizationGoogleStore.Label> = authorizationStore.labels


    fun onEvent(event: AuthorizationGoogleStore.Intent) {
        authorizationStore.accept(event)
    }

    fun onOutput(output: Output) {
        output(output)
    }

    sealed class Output {
        data class OpenAuthorizationPhoneScreen(val userData: UserData) : Output()
    }

}
