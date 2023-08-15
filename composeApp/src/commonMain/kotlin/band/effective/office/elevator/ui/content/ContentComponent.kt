package band.effective.office.elevator.ui.content

import band.effective.office.elevator.ui.booking.BookingComponent
import band.effective.office.elevator.ui.employee.FullEmployeeComponent
import band.effective.office.elevator.ui.employee.allEmployee.EmployeeComponent
import band.effective.office.elevator.ui.main.MainComponent
import band.effective.office.elevator.ui.profile.ProfileComponent
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.arkivanov.mvikotlin.core.store.StoreFactory

class ContentComponent(
    componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val openAuthorizationFlow: () -> Unit
) :
    ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()
    private val stack = childStack(
        source = navigation,
        initialStack = { listOf(Config.MainScreen) },
        childFactory = ::child,
    )
    val childStack: Value<ChildStack<*, Child>> = stack

    private fun child(config: Config, componentContext: ComponentContext): Child = when (config) {
        is Config.MainScreen -> Child.Main(MainComponent(componentContext, storeFactory))
        is Config.Profile -> Child.Profile(
            ProfileComponent(
                componentContext,
                storeFactory,
                openAuthorizationFlow
            )
        )
        is Config.Booking -> Child.Booking(BookingComponent(componentContext, storeFactory, ::bookingOutput))
        is Config.Employee -> Child.Employee(FullEmployeeComponent(componentContext, storeFactory))
    }

    private fun bookingOutput(output: BookingComponent.Output){
        when(output){
            BookingComponent.Output.OpenMainTab -> navigation.bringToFront(Config.MainScreen)
        }
    }

    fun onOutput(output: Output) {
        when (output) {
            Output.OpenMainTab -> navigation.bringToFront(Config.MainScreen)
            Output.OpenProfileTab -> navigation.bringToFront(Config.Profile)
            Output.OpenBookingTab -> navigation.bringToFront(Config.Booking)
            Output.OpenEmployeeTab -> navigation.bringToFront(Config.Employee)
        }
    }


    sealed class Child {
        class Main(val component: MainComponent) : Child()

        class Profile(val component: ProfileComponent) : Child()

        class Booking(val component: BookingComponent): Child()

        class Employee(val component: FullEmployeeComponent): Child()
    }

    private sealed interface Config : Parcelable {
        @Parcelize
        object MainScreen : Config

        @Parcelize
        object Booking : Config

        @Parcelize
        object Employee : Config

        @Parcelize
        object Profile : Config
    }

    sealed class Output {
        object OpenProfileTab : Output()
        object OpenMainTab : Output()
        object OpenEmployeeTab : Output()
        object OpenBookingTab : Output()
    }
}
