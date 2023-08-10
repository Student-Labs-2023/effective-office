package band.effective.office.tablet.ui.mainScreen.editBooking

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import band.effective.office.tablet.features.roomInfo.MainRes
import band.effective.office.tablet.ui.buttons.alert.AlertButton
import band.effective.office.tablet.ui.buttons.success.SuccessButton
import band.effective.office.tablet.ui.mainScreen.bookingRoomComponents.uiComponents.Alert
import band.effective.office.tablet.ui.mainScreen.bookingRoomComponents.uiComponents.DateTimeView
import band.effective.office.tablet.ui.mainScreen.bookingRoomComponents.uiComponents.EventDurationView
import band.effective.office.tablet.ui.mainScreen.bookingRoomComponents.uiComponents.EventOrganizerView
import band.effective.office.tablet.ui.theme.h7
import io.github.skeptick.libres.compose.painterResource
import java.util.Calendar


@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
@Composable
fun EditBookingView(modifier: Modifier = Modifier, editBookingComponent: EditBookingComponent) {
    val state by editBookingComponent.state.collectAsState()
    BookingRoomView(
        modifier = modifier,
        isSelectCurrentTime = state.isSelectCurrentTime,
        currentDate = state.currentDate,
        selectDate = state.selectDate,
        incrementDay = {},
        decrementDay = { },
        selectDuration = state.length,
        incrementDuration = {},
        decrementDuration = { },
        isBusy = state.isBusy,
        organizers = state.organizers,
        selectOrganizer = state.organizer,
        isExpandedOrganizersList = state.isExpandedOrganizersList,
        onExpandedChange = {},
        onSelectOrganizer = {},
        isOrganizerError = state.isOrganizerError,
        onRequestBookingCurrentRoom = { },
        onRequestBookingOtherRoom = { }
    )
}

@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
@Composable
fun BookingRoomView(
    modifier: Modifier = Modifier,
    isSelectCurrentTime: Boolean,
    currentDate: Calendar,
    selectDate: Calendar,
    incrementDay: () -> Unit,
    decrementDay: () -> Unit,
    selectDuration: Int,
    incrementDuration: () -> Unit,
    decrementDuration: () -> Unit,
    isBusy: Boolean,
    organizers: List<String>,
    selectOrganizer: String,
    isExpandedOrganizersList: Boolean,
    onExpandedChange: () -> Unit,
    onSelectOrganizer: (String) -> Unit,
    isOrganizerError: Boolean,
    onRequestBookingCurrentRoom: () -> Unit,
    onRequestBookingOtherRoom: () -> Unit
) {
    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colors.surface) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = MainRes.string.booking_view_title,
                style = MaterialTheme.typography.h3
            )
            Spacer(modifier = Modifier.height(25.dp))
            DateTimeView(
                modifier = Modifier.fillMaxWidth().height(100.dp),
                selectDate = if (isSelectCurrentTime) currentDate else selectDate,
                increment = { incrementDay() },
                decrement = { decrementDay() }
            )
            Spacer(modifier = Modifier.height(25.dp))
            EventDurationView(
                modifier = Modifier.fillMaxWidth().height(100.dp),
                currentDuration = selectDuration,
                increment = { incrementDuration() },
                decrement = { decrementDuration() }
            )
            if (isBusy) {
                Spacer(Modifier.height(10.dp))
                Alert(modifier = Modifier.fillMaxWidth(), text = MainRes.string.no_correct_time)
            }
            Spacer(modifier = Modifier.height(25.dp))
            EventOrganizerView(
                modifier = Modifier.fillMaxWidth().height(100.dp),
                organizers = organizers,
                expanded = isExpandedOrganizersList,
                selectedItem = selectOrganizer,
                onExpandedChange = { onExpandedChange() },
                onSelectItem = { onSelectOrganizer(it) }
            )
            if (isOrganizerError) {
                Spacer(Modifier.height(10.dp))
                Alert(
                    modifier = Modifier.fillMaxWidth(),
                    text = MainRes.string.no_select_organizer_alert
                )
            }
            Spacer(modifier = Modifier.height(50.dp))
        }
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            AlertButton(
                modifier = Modifier.fillMaxWidth().height(60.dp),
                onClick = { onRequestBookingOtherRoom() }
            ) {
                Text(text = MainRes.string.see_free_room, style = MaterialTheme.typography.h7)
                Image(
                    modifier = Modifier,
                    painter = painterResource(MainRes.image.arrow_right),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.onPrimary)
                )
            }
            Spacer(Modifier.height(10.dp))

            SuccessButton(
                modifier = Modifier.fillMaxWidth().height(60.dp).focusable(true),
                onClick = { onRequestBookingCurrentRoom() }
            ) {
                Text(
                    text = "Изменить",
                    style = MaterialTheme.typography.h6
                )
            }
        }
    }
}