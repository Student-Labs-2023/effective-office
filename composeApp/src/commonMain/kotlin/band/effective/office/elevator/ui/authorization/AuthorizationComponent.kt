package band.effective.office.elevator.ui.authorization

import band.effective.office.elevator.domain.entity.AuthorizationEntity
import band.effective.office.elevator.domain.models.UserData
import band.effective.office.elevator.expects.showToast
import band.effective.office.elevator.ui.authorization.authorization_google.AuthorizationGoogleComponent
import band.effective.office.elevator.ui.authorization.authorization_phone.AuthorizationPhoneComponent
import band.effective.office.elevator.ui.authorization.authorization_profile.AuthorizationProfileComponent
import band.effective.office.elevator.ui.authorization.authorization_telegram.AuthorizationTelegramComponent
import band.effective.office.elevator.ui.authorization.store.AuthorizationStore
import band.effective.office.elevator.ui.authorization.store.AuthorizationStoreFactory
import band.effective.office.elevator.ui.models.validator.Validator
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AuthorizationComponent(
    componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val openContentFlow: () -> Unit
) :
    ComponentContext by componentContext, KoinComponent {

    private val validator: Validator = Validator()
    private val navigation = StackNavigation<AuthorizationComponent.Config>()
    private val userData: UserData = UserData()
    private val authorizationEntity: AuthorizationEntity by inject()

    private fun changePhoneNumber(phoneNumber: String) {
        userData.phoneNumber = phoneNumber
    }

    private fun changeName(name: String) {
        userData.name = name
    }

    private fun changePost(post: String) {
        userData.post = post
    }

    private fun changeTelegramNick(telegramNick: String) {
        userData.telegramNick = telegramNick
    }

    private val authorizationStore =
        instanceKeeper.getStore {
            AuthorizationStoreFactory(
                storeFactory = storeFactory
            ).create()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    val state: StateFlow<AuthorizationStore.State> = authorizationStore.stateFlow

    val label: Flow<AuthorizationStore.Label> = authorizationStore.labels

    private val stack = childStack(
        source = navigation,
        initialStack = { listOf(AuthorizationComponent.Config.GoogleAuth) },
        childFactory = ::child,
        handleBackButton = true
    )

    val childStack: Value<ChildStack<*, AuthorizationComponent.Child>> = stack

    private fun child(
        config: AuthorizationComponent.Config,
        componentContext: ComponentContext
    ): AuthorizationComponent.Child =
        when (config) {
            is Config.GoogleAuth -> Child.GoogleAuthChild(
                AuthorizationGoogleComponent(
                    componentContext,
                    storeFactory,
                    ::googleAuthOutput
                )
            )

            is Config.PhoneAuth -> Child.PhoneAuthChild(
                AuthorizationPhoneComponent(
                    componentContext,
                    storeFactory,
                    validator,
                    userData.phoneNumber,
                    ::phoneAuthOutput,
                    ::changePhoneNumber
                )
            )

            is Config.ProfileAuth -> Child.ProfileAuthChild(
                AuthorizationProfileComponent(
                    componentContext,
                    storeFactory,
                    validator,
                    userData.name,
                    userData.post!!,
                    ::profileAuthOutput,
                    ::changeName,
                    ::changePost
                )
            )

            is Config.TelegramAuth -> Child.TelegramAuthChild(
                AuthorizationTelegramComponent(
                    componentContext,
                    storeFactory,
                    validator,
                    userData.telegramNick,
                    ::telegramAuthOutput,
                    ::changeTelegramNick
                )
            )
        }

    private fun googleAuthOutput(output: AuthorizationGoogleComponent.Output) {
        when (output) {
            is AuthorizationGoogleComponent.Output.OpenAuthorizationPhoneScreen -> navigation.replaceAll(
                Config.PhoneAuth
            )
        }
    }

    private fun phoneAuthOutput(output: AuthorizationPhoneComponent.Output) {
        when (output) {
            is AuthorizationPhoneComponent.Output.OpenProfileScreen -> navigation.bringToFront(
                Config.ProfileAuth
            )

            is AuthorizationPhoneComponent.Output.OpenGoogleScreen -> navigation.bringToFront(
                AuthorizationComponent.Config.GoogleAuth
            )
        }
    }

    private fun profileAuthOutput(output: AuthorizationProfileComponent.Output) {
        when (output) {
            is AuthorizationProfileComponent.Output.OpenPhoneScreen -> navigation.bringToFront(
                Config.PhoneAuth
            )

            is AuthorizationProfileComponent.Output.OpenTGScreen -> navigation.bringToFront(
                Config.TelegramAuth
            )
        }
    }

    private fun telegramAuthOutput(output: AuthorizationTelegramComponent.Output) {
        when (output) {
            is AuthorizationTelegramComponent.Output.OpenProfileScreen -> navigation.bringToFront(
                Config.ProfileAuth
            )

            is AuthorizationTelegramComponent.Output.OpenContentFlow -> {
                CoroutineScope(Dispatchers.Main).launch {
                    val result = authorizationEntity.push(userData)
                    if (result)
                        openContentFlow()
                }
            }
        }
    }

    sealed class Child {
        class GoogleAuthChild(val component: AuthorizationGoogleComponent) : Child()
        class PhoneAuthChild(val component: AuthorizationPhoneComponent) : Child()
        class ProfileAuthChild(val component: AuthorizationProfileComponent) : Child()
        class TelegramAuthChild(val component: AuthorizationTelegramComponent) : Child()
    }

    sealed class Config : Parcelable {
        @Parcelize
        object GoogleAuth : Config()

        @Parcelize
        object PhoneAuth : Config()

        @Parcelize
        object ProfileAuth : Config()

        @Parcelize
        object TelegramAuth : Config()
    }
}