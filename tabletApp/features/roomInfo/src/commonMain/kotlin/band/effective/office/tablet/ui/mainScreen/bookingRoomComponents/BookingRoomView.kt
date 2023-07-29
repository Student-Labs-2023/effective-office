package band.effective.office.tablet.ui.mainScreen.bookingRoomComponents

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import band.effective.office.tablet.features.roomInfo.MainRes
import band.effective.office.tablet.ui.mainScreen.bookingRoomComponents.store.BookingStore
import band.effective.office.tablet.ui.mainScreen.bookingRoomComponents.uiComponents.Alert
import band.effective.office.tablet.ui.mainScreen.bookingRoomComponents.uiComponents.DateTimeView
import band.effective.office.tablet.ui.mainScreen.bookingRoomComponents.uiComponents.EventDurationView
import band.effective.office.tablet.ui.mainScreen.bookingRoomComponents.uiComponents.EventOrganizerView
import io.github.skeptick.libres.compose.painterResource
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
@Composable
fun BookingRoomView(modifier: Modifier = Modifier, bookingRoomComponent: BookingRoomComponent) {
    val state by bookingRoomComponent.state.collectAsState()
    BookingRoomView(
        modifier = modifier,
        isSelectCurrentTime = state.isSelectCurrentTime,
        currentDate = state.currentDate,
        selectDate = state.selectDate,
        incrementDay = { bookingRoomComponent.sendIntent(BookingStore.Intent.OnChangeDate(1)) },
        decrementDay = { bookingRoomComponent.sendIntent(BookingStore.Intent.OnChangeDate(-1)) },
        selectDuration = state.length,
        incrementDuration = { bookingRoomComponent.sendIntent(BookingStore.Intent.OnChangeLength(30)) },
        decrementDuration = { bookingRoomComponent.sendIntent(BookingStore.Intent.OnChangeLength(-15)) },
        isBusy = state.isBusy,
        organizers = state.organizers,
        selectOrganizer = state.organizer,
        isExpandedOrganizersList = state.isExpandedOrganizersList,
        onExpandedChange = { bookingRoomComponent.sendIntent(BookingStore.Intent.OnChangeExpanded) },
        onSelectOrganizer = {
            bookingRoomComponent.sendIntent(
                BookingStore.Intent.OnChangeOrganizer(it)
            )
        },
        isOrganizerError = state.isOrganizerError,
        onRequestBookingCurrentRoom = { bookingRoomComponent.sendIntent(BookingStore.Intent.OnBookingCurrentRoom) },
        onRequestBookingOtherRoom = { bookingRoomComponent.sendIntent(BookingStore.Intent.OnBookingOtherRoom) },
        roomName = state.roomName
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
    onRequestBookingOtherRoom: () -> Unit,
    roomName: String
) {
    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colors.surface) {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(63.dp))
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
            if (isBusy) {
                Button(
                    modifier = Modifier.fillMaxWidth().height(60.dp)
                        .clip(RoundedCornerShape(100.dp)).border(
                            width = 3.dp,
                            shape = RoundedCornerShape(100.dp),
                            color = MaterialTheme.colors.onPrimary
                        ),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                    onClick = { onRequestBookingOtherRoom() }
                ) {
                    Text(text = MainRes.string.see_free_room)
                    Image(
                        modifier = Modifier,
                        painter = painterResource(MainRes.image.arrow_right),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(MaterialTheme.colors.onPrimary)
                    )
                }
                Spacer(Modifier.height(10.dp))
            }
            Button(
                modifier = Modifier.fillMaxWidth().height(60.dp).clip(RoundedCornerShape(100.dp))
                    .focusable(true),
                onClick = { onRequestBookingCurrentRoom() }
            ) {
                Text(text = MainRes.string.booking_button_text.format(roomName = roomName))
            }
        }
    }
}