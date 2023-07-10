package tablet.ui.selectRoomScreen

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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import band.effective.office.tablet.features.selectRoom.MainRes
import tablet.ui.selectRoomScreen.uiComponents.BookingButtonView
import tablet.ui.selectRoomScreen.uiComponents.CrossButtonView
import tablet.ui.selectRoomScreen.uiComponents.DateTimeView
import tablet.ui.selectRoomScreen.uiComponents.LengthEventView
import tablet.ui.selectRoomScreen.uiComponents.OrganizerEventView
import tablet.ui.selectRoomScreen.uiComponents.Title
import tablet.ui.selectRoomScreen.uiComponents.TitleFieldView

@Composable
fun CheckButton(component: RealSelectRoomComponent) {
    val showDialog = remember { mutableStateOf(false) }
    Button(
        onClick = { showDialog.value = true }
    ) {
        Text(
            text = "check",
        )
    }

    if(showDialog.value){
        SelectRoomView(component, showDialog)
    }
}

@Composable
fun SelectRoomView(
    component: RealSelectRoomComponent,
    showDialog: MutableState<Boolean>
) {
   // val showDialog = remember { mutableStateOf(true) }
    val modifier = Modifier.background(Color(0xFF3A3736))
    val shape = RoundedCornerShape(16)

    if (!showDialog.value) return

    Dialog(
        onDismissRequest = { showDialog.value = false }
    )
    {
        Box(
            modifier = Modifier
                .size(575.dp, 510.dp)
                .clip(RoundedCornerShape(5))
                .background(Color(0xFF302D2C)),
        ) {
            Column(
                modifier = Modifier.matchParentSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.height(30.dp))
                CrossButtonView(
                    Modifier.width(575.dp),
                    onDismissRequest = { showDialog.value = false }
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
                    color = Color(0xFFEF7234),
                    shape = RoundedCornerShape(40),
                    booking = component.booking
                )
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun RowInfoLengthAndOrganizer(modifier: Modifier, shape: RoundedCornerShape, component: RealSelectRoomComponent){
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