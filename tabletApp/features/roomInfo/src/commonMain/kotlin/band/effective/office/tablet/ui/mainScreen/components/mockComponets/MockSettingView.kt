package band.effective.office.tablet.ui.mainScreen.components.mockComponets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MockSettingView(component: MockSettingsComponent) {
    val state by component.state.collectAsState()
    Surface(
        modifier = Modifier.fillMaxWidth().height(100.dp),
        color = Color(0xFF252322),
        onClick = { component.sendEvent(MockSettingsEvent.OnSwitchVisible) }) {
        if (state.isVisible) {
            Column {
                Row {
                    Item(
                        checked = state.isBusy,
                        onCheckedChange = { component.sendEvent(MockSettingsEvent.OnSwitchBusy(it)) },
                        text = "Комната занята"
                    )
                    Item(
                        checked = state.isManyEvent,
                        onCheckedChange = {
                            component.sendEvent(
                                MockSettingsEvent.OnSwitchEventCount(
                                    it
                                )
                            )
                        },
                        text = "Много меропритий"
                    )
                    Item(
                        checked = state.isHaveTv,
                        onCheckedChange = { component.sendEvent(MockSettingsEvent.OnSwitchTv(it)) },
                        text = "Есть tv"
                    )
                }
            }
        }
    }
}

@Composable
private fun Item(checked: Boolean, onCheckedChange: (Boolean) -> Unit, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = { onCheckedChange(it) },
            colors = CheckboxDefaults.colors(uncheckedColor = Color.White),
        )
        Text(text = text, color = Color.White)
    }
}