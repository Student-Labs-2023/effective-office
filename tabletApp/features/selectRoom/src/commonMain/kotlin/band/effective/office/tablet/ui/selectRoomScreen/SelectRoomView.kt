package band.effective.office.tablet.ui.selectRoomScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import band.effective.office.tablet.features.selectRoom.MainRes
import band.effective.office.tablet.ui.selectRoomScreen.uiComponents.BookingButtonView
import band.effective.office.tablet.ui.selectRoomScreen.uiComponents.CrossButtonView
import band.effective.office.tablet.ui.selectRoomScreen.uiComponents.DateTimeView
import band.effective.office.tablet.ui.selectRoomScreen.uiComponents.LengthEventView
import band.effective.office.tablet.ui.selectRoomScreen.uiComponents.OrganizerEventView
import band.effective.office.tablet.ui.selectRoomScreen.uiComponents.Title
import band.effective.office.tablet.ui.selectRoomScreen.uiComponents.TitleFieldView
import band.effective.office.tablet.ui.theme.CustomDarkColors
import band.effective.office.tablet.ui.theme.LocalCustomColorsPalette

@Composable
fun CheckButton(component: SelectRoomComponentImpl) {
    val showDialog = remember { mutableStateOf(false) }
    Button(
        onClick = { showDialog.value = true }
    ) {
        Text(
            text = "check",
        )
    }

    if (showDialog.value) {
        SelectRoomView(component)
    }
}

@Composable
fun SelectRoomView(
    component: SelectRoomComponentImpl
) {
    // val showDialog = remember { mutableStateOf(true) }
    val modifier = Modifier.background(LocalCustomColorsPalette.current.mountainBackground)
    val shape = RoundedCornerShape(16)

    Dialog(
        onDismissRequest = { component.close() }
    )
    {
        Box(
            modifier = Modifier
                .size(575.dp, 510.dp)
                .clip(RoundedCornerShape(5))
                .background(LocalCustomColorsPalette.current.elevationBackground),
        ) {
            Column(
                modifier = Modifier.matchParentSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.height(30.dp))
                CrossButtonView(
                    Modifier.width(575.dp),
                    onDismissRequest = { component.close() }
                )
                Title(component)
                Spacer(modifier = Modifier.height(24.dp))
                TitleFieldView(
                    modifier = Modifier.width(415.dp),
                    title = MainRes.string.whenEvent
                )
                Spacer(modifier = Modifier.height(16.dp))
                DateTimeView(
                    modifier = modifier.height(64.dp).width(415.dp),
                    shape = shape,
                    booking = component.booking
                )
                Spacer(modifier = Modifier.height(24.dp))
                RowInfoLengthAndOrganizer(modifier, shape, component)
                Spacer(modifier = Modifier.height(40.dp))
                BookingButtonView(
                    modifier = Modifier.height(64.dp).width(415.dp),
                    shape = RoundedCornerShape(40),
                    booking = component.booking
                )
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun RowInfoLengthAndOrganizer(
    modifier: Modifier,
    shape: RoundedCornerShape,
    component: SelectRoomComponentImpl
) {
    Row {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            TitleFieldView(
                modifier = Modifier.width(156.dp),
                title = MainRes.string.how_much
            )
            LengthEventView(
                modifier = modifier.height(64.dp).width(156.dp),
                shape = shape,
                booking = component.booking
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            TitleFieldView(
                modifier = Modifier.width(243.dp),
                title = MainRes.string.organizer
            )
            OrganizerEventView(
                modifier = modifier.height(64.dp).width(243.dp),
                shape = shape,
                booking = component.booking
            )
        }
    }
}