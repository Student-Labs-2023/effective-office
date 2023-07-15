package band.effective.office.elevator.ui.authorization.authorizationphone_page

import band.effective.office.elevator.ui.authorization.authorizationphone_page.AuthorizationPhoneStore.*
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

internal class AuthorizationPhoneStoreFactory(private val storeFactory: StoreFactory) :
    KoinComponent {

    fun create(): AuthorizationPhoneStore =
        object : AuthorizationPhoneStore,
            Store<Intent, State, Label> by storeFactory.create(
                name = "Authorization phone",
                initialState = State(),
                bootstrapper = BootstrapperImpl(),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl
            ) {
        }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {

            }
        }
    }

    private sealed interface Action {

    }

    private object ReducerImpl : Reducer<State, Intent> {
        override fun State.reduce(msg: Intent): State =
            when (msg) {
                Intent.BackButtonClicked -> TODO()
                Intent.ContinueButtonClicked -> TODO()
                is Intent.PhoneNumberChanged -> TODO()
            }

        private fun validatePhoneNumber(phoneNumber: String) = phoneNumber.length == 11
    }

    private class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Nothing, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) =
            when (intent) {
                Intent.BackButtonClicked -> TODO()
                Intent.ContinueButtonClicked -> TODO()
                is Intent.PhoneNumberChanged -> TODO()
            }
    }
}