package band.effective.office.elevator.ui.profile

import band.effective.office.elevator.ui.models.User
import band.effective.office.elevator.ui.profile.editProfile.ProfileEditComponent
import band.effective.office.elevator.ui.profile.mainProfile.MainProfileComponent
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.arkivanov.mvikotlin.core.store.StoreFactory

class ProfileComponent(
    componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val openAuthorizationFlow: () -> Unit) :
    ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    private val stack = childStack(
        source = navigation,
        initialStack = { listOf(Config.MainProfile) },
        childFactory = ::child,
        handleBackButton = true
    )

    val childStack: Value<ChildStack<*,Child>> = stack

    private fun child(config: Config, componentContext: ComponentContext):Child =
        when(config){
            is Config.MainProfile -> Child.MainProfileChild(
                MainProfileComponent(
                    componentContext,
                    storeFactory,
                    ::mainProfileOutput,
                )
            )
            is Config.EditProfile -> Child.EditProfileChild(
                ProfileEditComponent(
                    componentContext,
                    storeFactory,
                    ::editProfileOutput,
                    config.user
                )
            )
        }

    private fun editProfileOutput(output: ProfileEditComponent.Output) {
        when(output){
            ProfileEditComponent.Output.NavigationBack -> navigation.replaceAll(Config.MainProfile)
        }
    }

    private fun mainProfileOutput(output: MainProfileComponent.Output) {
        when(output){
            is MainProfileComponent.Output.OpenAuthorizationFlow -> openAuthorizationFlow()
            is MainProfileComponent.Output.NavigateToEdit -> navigation.push(Config.EditProfile(output.userEdit))
        }
    }

    sealed class Child{
        class MainProfileChild(val component: MainProfileComponent) : Child()
        class EditProfileChild(val component: ProfileEditComponent): Child()
    }

    sealed class Config: Parcelable{
        @Parcelize
        object MainProfile: Config()

        @Parcelize
        data class EditProfile(val user: User): Config()
    }
}
