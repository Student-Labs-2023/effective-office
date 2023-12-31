package band.effective.office.tablet.ui.mainScreen.roomInfoComponents

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import band.effective.office.tablet.domain.model.EventInfo
import band.effective.office.tablet.domain.model.RoomInfo
import band.effective.office.tablet.ui.mainScreen.roomInfoComponents.store.RoomInfoStore
import band.effective.office.tablet.ui.mainScreen.roomInfoComponents.uiComponent.BusyRoomInfoComponent
import band.effective.office.tablet.ui.mainScreen.roomInfoComponents.uiComponent.DateTimeComponent
import band.effective.office.tablet.ui.mainScreen.roomInfoComponents.uiComponent.FreeRoomInfoComponent
import band.effective.office.tablet.ui.mainScreen.roomInfoComponents.uiComponent.IconSettingsView
import band.effective.office.tablet.ui.mainScreen.roomInfoComponents.uiComponent.RoomEventListComponent
import band.effective.office.tablet.utils.oneDay
import java.util.Calendar
import java.util.GregorianCalendar

@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
@Composable
fun RoomInfoComponent(
    modifier: Modifier,
    roomInfoComponent: RoomInfoComponent,
    onEventUpdateRequest: (EventInfo) -> Unit,
    onSettings: () -> Unit
) {
    val state by roomInfoComponent.state.collectAsState()
    RoomInfoComponent(
        modifier = modifier,
        room = state.roomInfo,
        onOpenModalRequest = { roomInfoComponent.sendIntent(RoomInfoStore.Intent.OnFreeRoomRequest) },
        timeToNextEvent = state.changeEventTime,
        isToday = state.selectDate.isToday(),
        isError = state.isError,
        nextEvent = state.nextEvent,
        onEventUpdateRequest = onEventUpdateRequest,
        onSettings = onSettings
    )
}

@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
@Composable
fun RoomInfoComponent(
    modifier: Modifier = Modifier,
    room: RoomInfo,
    onOpenModalRequest: () -> Unit,
    timeToNextEvent: Int,
    isToday: Boolean,
    isError: Boolean,
    nextEvent: EventInfo,
    onEventUpdateRequest: (EventInfo) -> Unit,
    onSettings: () -> Unit
) {
    val paddings = 30.dp
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            DateTimeComponent(
                modifier = Modifier.padding(paddings)
            )
            Spacer(modifier = Modifier.weight(1f))
            IconSettingsView(
                modifier = Modifier.padding(end = paddings),
                onSettings = onSettings
            )
        }
        when {
            room.isFree() -> {
                FreeRoomInfoComponent(
                    modifier = Modifier.padding(paddings),
                    name = room.name,
                    capacity = room.capacity,
                    isHaveTv = room.isHaveTv,
                    electricSocketCount = room.socketCount,
                    nextEvent = nextEvent,
                    timeToNextEvent = timeToNextEvent,
                    isError = isError
                )
            }

            room.isBusy() -> {
                BusyRoomInfoComponent(
                    modifier = Modifier.padding(paddings),
                    name = room.name,
                    capacity = room.capacity,
                    isHaveTv = room.isHaveTv,
                    electricSocketCount = room.socketCount,
                    event = room.currentEvent ?: EventInfo.emptyEvent,
                    onButtonClick = { onOpenModalRequest() },
                    timeToFinish = timeToNextEvent,
                    isError = isError
                )
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
        RoomEventListComponent(
            modifier = Modifier.padding(paddings),
            eventsList = room.eventList,
            isToday = isToday,
            onItemClick = onEventUpdateRequest
        )
    }
}

private fun RoomInfo.isFree() = currentEvent == null
private fun RoomInfo.isBusy() = currentEvent != null
private fun Calendar.isToday(): Boolean = oneDay(GregorianCalendar())