package band.effective.office.elevator.ui.authorization.authorization_phone

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import band.effective.office.elevator.ExtendedTheme
import band.effective.office.elevator.MainRes
import band.effective.office.elevator.components.EffectiveButton
import band.effective.office.elevator.components.OutlinedTextColorsSetup
import band.effective.office.elevator.expects.showToast
import band.effective.office.elevator.textGrayColor
import band.effective.office.elevator.theme_light_primary_stroke
import band.effective.office.elevator.ui.authorization.authorization_phone.store.AuthorizationPhoneStore
import band.effective.office.elevator.ui.authorization.components.AuthSubTitle
import band.effective.office.elevator.ui.authorization.components.AuthTabRow
import band.effective.office.elevator.ui.authorization.components.AuthTitle
import band.effective.office.elevator.ui.models.PhoneMaskTransformation
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun AuthorizationPhoneScreen(component: AuthorizationPhoneComponent) {

    val state by component.phone.collectAsState()
    val errorMessage = stringResource(MainRes.strings.number_format_error)

    LaunchedEffect(component) {
        component.label.collect { label ->
            when (label) {
                AuthorizationPhoneStore.Label.AuthorizationPhoneFailure -> {
                    showToast(errorMessage)
                }

                is AuthorizationPhoneStore.Label.AuthorizationPhoneSuccess -> {
                    component.change(state.phoneNumber)
                    component.onOutput(
                        AuthorizationPhoneComponent.Output.OpenProfileScreen
                    )
                }

                AuthorizationPhoneStore.Label.ReturnInGoogleAuthorization -> component.onOutput(
                    AuthorizationPhoneComponent.Output.OpenGoogleScreen
                )
            }
        }
    }

    AuthorizationPhoneComponent(onEvent = component::onEvent, state)
}

@Composable
private fun AuthorizationPhoneComponent(
    onEvent: (AuthorizationPhoneStore.Intent) -> Unit,
    state: AuthorizationPhoneStore.State
) {
    val elevation = ButtonDefaults.elevation(
        defaultElevation = 0.dp,
        pressedElevation = 0.dp,
        disabledElevation = 0.dp,
        hoveredElevation = 0.dp,
        focusedElevation = 0.dp
    )

    val closeIcon = remember { mutableStateOf(false) }
    val borderColor = remember { mutableStateOf(textGrayColor) }
    val leadingColor = remember { mutableStateOf(textGrayColor) }

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(
                horizontal = 16.dp,
                vertical = 48.dp
            ),
    ) {
        IconButton(
            modifier = Modifier.size(size = 48.dp),
            onClick = {
                onEvent(AuthorizationPhoneStore.Intent.BackButtonClicked)
            }) {
            Icon(
                imageVector = Icons.Rounded.ArrowBack,
                tint = Color.Black,
                contentDescription = "back screen arrow"
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        AuthTabRow(0)

        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            AuthTitle(
                text = stringResource(MainRes.strings.input_number),
                modifier = Modifier.padding(bottom = 7.dp),
                textAlign = TextAlign.Start
            )

            AuthSubTitle(
                text = stringResource(MainRes.strings.select_number),
                modifier = Modifier.padding(bottom = 24.dp),
                textAlign = TextAlign.Start
            )

            OutlinedTextField(
                value = state.phoneNumber,
                onValueChange = {
                    if (it.isNotEmpty()) {
                        closeIcon.value = true
                        leadingColor.value = Color.Black
                        borderColor.value = theme_light_primary_stroke
                    } else {
                        borderColor.value = textGrayColor
                        closeIcon.value = false
                        leadingColor.value = textGrayColor
                    }
                    onEvent(
                        AuthorizationPhoneStore.Intent.PhoneNumberChanged(phoneNumber = it)
                    )
                },
                visualTransformation = PhoneMaskTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                textStyle = MaterialTheme.typography.body1,
                colors = OutlinedTextColorsSetup(),
                placeholder = {
                    Text(
                        text = stringResource(MainRes.strings.number_hint),
                        style = MaterialTheme.typography.button,
                        color = textGrayColor
                    )
                },
                isError = state.isErrorPhoneNumber,
                singleLine = true,
                trailingIcon = {
                    if (closeIcon.value) {
                        IconButton(onClick = {
                            onEvent(
                                AuthorizationPhoneStore.Intent.PhoneNumberChanged(phoneNumber = "")
                            )
                            closeIcon.value = false
                            leadingColor.value = textGrayColor
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.Close,
                                contentDescription = "clear text field",
                            )
                        }
                    }
                },
                shape = RoundedCornerShape(12.dp),
                leadingIcon = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = stringResource(MainRes.strings.phone_plus_seven),
                            style = MaterialTheme.typography.button,
                            color = leadingColor.value
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Divider(
                            modifier = Modifier
                                .height(20.dp)
                                .width(2.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (state.isErrorPhoneNumber) ExtendedTheme.colors.error else borderColor.value)
                                .padding(vertical = 14.dp)
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .onFocusChanged {
                        if (it.isFocused) {
                            borderColor.value = theme_light_primary_stroke
                        } else {
                            borderColor.value = textGrayColor
                            leadingColor.value = textGrayColor
                        }
                    }
            )

            Spacer(modifier = Modifier.height(16.dp))

            EffectiveButton(
                buttonText = stringResource(MainRes.strings._continue),
                onClick = {
                    onEvent(AuthorizationPhoneStore.Intent.ContinueButtonClicked)
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}