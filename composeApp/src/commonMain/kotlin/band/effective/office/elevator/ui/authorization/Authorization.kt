package band.effective.office.elevator.ui.authorization

import androidx.compose.runtime.Composable
import band.effective.office.elevator.ui.authorization.authorization_google.AuthorizationGoogleScreen
import band.effective.office.elevator.ui.authorization.authorization_phone.AuthorizationPhoneScreen
import band.effective.office.elevator.ui.authorization.authorization_profile.AuthorizationProfileScreen
import band.effective.office.elevator.ui.authorization.authorization_telegram.AuthorizationTelegramScreen
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation

@Composable
fun AuthorizationScreen(component: AuthorizationComponent) {
    Children(
        stack = component.childStack,
        animation = stackAnimation(fade() + scale()),
    ) {
        when (val child = it.instance) {
            is AuthorizationComponent.Child.GoogleAuthChild -> AuthorizationGoogleScreen(child.component)
            is AuthorizationComponent.Child.PhoneAuthChild -> AuthorizationPhoneScreen(child.component)
            is AuthorizationComponent.Child.ProfileAuthChild -> AuthorizationProfileScreen(child.component)
            is AuthorizationComponent.Child.TelegramAuthChild -> AuthorizationTelegramScreen(child.component)
        }
    }
}