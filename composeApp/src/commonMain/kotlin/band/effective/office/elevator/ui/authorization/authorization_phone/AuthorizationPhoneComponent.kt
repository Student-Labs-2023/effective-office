package band.effective.office.elevator.ui.authorization.authorization_phone

import band.effective.office.elevator.domain.models.UserData
import band.effective.office.elevator.ui.authorization.authorization_phone.store.AuthorizationPhoneStore
import band.effective.office.elevator.ui.authorization.authorization_phone.store.AuthorizationPhoneStoreFactory
import band.effective.office.elevator.ui.models.validator.Validator
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlin.reflect.KFunction1

class AuthorizationPhoneComponent(
    componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val validator: Validator,
    private val userData: UserData,
    private val output: (AuthorizationPhoneComponent.Output) -> Unit,
    private val function: (String) -> Unit
) : ComponentContext by componentContext {

    private val authorizationPhoneStore =
        instanceKeeper.getStore {
            AuthorizationPhoneStoreFactory(
                storeFactory = storeFactory,
                validator,
                userData
            ).create()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    val phone: StateFlow<AuthorizationPhoneStore.State> = authorizationPhoneStore.stateFlow

    val label: Flow<AuthorizationPhoneStore.Label> = authorizationPhoneStore.labels

    fun onEvent(event: AuthorizationPhoneStore.Intent) {
        authorizationPhoneStore.accept(event)
    }

    fun onOutput(output: Output) {
        output(output)
    }

    fun change(phoneNumber: String) = function(phoneNumber)

    sealed class Output {
        data class OpenProfileScreen(val userData: UserData) : Output()

        object OpenGoogleScreen : Output()
    }
}
