package band.effective.office.elevator.ui.authorization.authorization_telegram.store

import com.arkivanov.mvikotlin.core.store.Store

interface AuthorizationTelegramStore :
    Store<AuthorizationTelegramStore.Intent, AuthorizationTelegramStore.State, AuthorizationTelegramStore.Label> {

    sealed interface Intent {
        data class NickChanged(val name: String) : Intent
        object ContinueButtonClicked : Intent
        object BackButtonClicked : Intent
    }

    data class State(
        var name: String = "Никнейм в телеграм",
        var isLoading: Boolean = false,
        var isError: Boolean = false
    )

    sealed interface Label {
        object AuthorizationTelegramSuccess : Label

        object AuthorizationTelegramFailure : Label

        object ReturnInProfileAuthorization : Label

        object OpenMainScreen : Label
    }
}