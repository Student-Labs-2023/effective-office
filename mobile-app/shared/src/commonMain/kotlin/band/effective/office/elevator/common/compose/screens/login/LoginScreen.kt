package band.effective.office.elevator.common.compose.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import band.effective.office.elevator.common.compose.expects.showToast
import band.effective.office.elevator.common.compose.imageResource
import kotlinx.coroutines.flow.collectLatest

@Composable
internal fun LoginScreen(onSignInSuccess: () -> Unit) {
    val viewModel = LoginViewModel()

    LaunchedEffect(key1 = Unit) {
        viewModel.effectState.collectLatest { effect ->
            when (effect) {
                is LoginViewModel.Effect.SignInFailure -> showToast(effect.message)
                LoginViewModel.Effect.SignInSuccess -> {
                    showToast("Successfully authorization")
                    onSignInSuccess()
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        GoogleSignInButton(modifier = Modifier.align(Alignment.Center), onClick = {
            viewModel.sendAction(
                LoginViewModel.Action.SignIn
            )
        })
    }
}

@Composable
internal fun GoogleSignInButton(modifier: Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(6.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Black, contentColor = Color.White
        )
    ) {
        Image(
            imageResource("google"), contentDescription = null, modifier = Modifier.size(32.dp)
        )
        Text(text = "Sign in with Google", modifier = Modifier.padding(6.dp))
    }
}
