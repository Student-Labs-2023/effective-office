package band.effective.office.tablet.ui.mainScreen.roomInfoComponents.uiComponent

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import band.effective.office.tablet.domain.model.EventInfo
import band.effective.office.tablet.features.roomInfo.MainRes
import band.effective.office.tablet.ui.theme.LocalCustomColorsPalette
import band.effective.office.tablet.ui.theme.roomInfoColor
import band.effective.office.tablet.utils.CalendarStringConverter
import java.util.Calendar

@Composable
fun BusyRoomInfoComponent(
    modifier: Modifier = Modifier,
    name: String,
    capacity: Int,
    isHaveTv: Boolean,
    electricSocketCount: Int,
    event: EventInfo?,
    onButtonClick: () -> Unit,
    timeToFinish: Int
) {
    val backgroundColor = LocalCustomColorsPalette.current.busyStatus

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val colorButton =  if(isPressed) roomInfoColor else backgroundColor
    val colorTextButton = if(isPressed) backgroundColor else roomInfoColor

    Surface {
        CommonRoomInfoComponent(
            modifier = modifier,
            name = name,
            capacity = capacity,
            isHaveTv = isHaveTv,
            electricSocketCount = electricSocketCount,
            backgroundColor = backgroundColor
        ){
            Text(
                text = MainRes.string.room_occupancy.format(
                    finishTime = event?.finishTime?.time() ?: "",
                    organizer = event?.organizer ?: ""
                ),
                style = MaterialTheme.typography.h5,
                color = roomInfoColor
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "${MainRes.string.busy_duration_string} ${timeToFinish.getDuration()}",
                style = MaterialTheme.typography.h5,
                color = roomInfoColor
            )
        }
        Box(
            modifier = modifier.fillMaxWidth(),
            contentAlignment = Alignment.TopEnd
        ) {
            Button(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(70.dp))
                    .height(60.dp)
                    .width(150.dp)
                    .background(color = backgroundColor).border(
                        width = 3.dp,
                        color = roomInfoColor,
                        shape = RoundedCornerShape(70.dp),
                    ),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = colorButton
                ),
                interactionSource = interactionSource,
                onClick = {
                    onButtonClick()
                }) {
                Text(text = MainRes.string.stop_meeting_button, color = colorTextButton)
            }
        }
    }
}

private fun Calendar.time() = CalendarStringConverter.calendarToString(this, "HH:mm")