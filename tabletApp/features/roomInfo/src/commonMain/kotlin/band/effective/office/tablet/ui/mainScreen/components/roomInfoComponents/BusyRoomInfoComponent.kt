package band.effective.office.tablet.ui.mainScreen.components.roomInfoComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import band.effective.office.tablet.domain.model.EventInfo
import band.effective.office.tablet.features.roomInfo.MainRes
import band.effective.office.tablet.utils.CalendarStringConverter
import java.util.Calendar

@Composable
fun BusyRoomInfoComponent(
    modifier: Modifier = Modifier,
    name: String,
    capacity: Int,
    isHaveTv: Boolean,
    electricSocketCount: Int,
    event: band.effective.office.tablet.domain.model.EventInfo?
) {
    val backgroundColor = Color(0xFFF94C4C)
    Surface {
        CommonRoomInfoComponent(
            modifier = modifier,
            name = name,
            capacity = capacity,
            isHaveTv = isHaveTv,
            electricSocketCount = electricSocketCount,
            roomOccupancy = MainRes.string.room_occupancy.format(
                startTime = event?.startTime?.time() ?: "",
                finishTime = event?.finishTime?.time() ?: "",
                organizer = event?.organizer ?: ""
            ),
            backgroundColor = backgroundColor
        )
        Box(
            modifier = modifier.fillMaxWidth(),
            contentAlignment = Alignment.TopEnd
        ){
            Button(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(70.dp))
                    .height(60.dp)
                    .width(150.dp)
                    .background(color = backgroundColor).border(
                        width = 3.dp,
                        color = Color(0xFFFAFAFA),
                        shape = RoundedCornerShape(70.dp),
                    ),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = backgroundColor,
                    contentColor = Color(0xFFFFFFFF)
                ),
                onClick = {}) {
                Text(text = MainRes.string.stop_meeting_button, color = Color(0xFFFAFAFA))
            }
        }
    }

}

private fun Calendar.time() = CalendarStringConverter.calendarToString(this, "HH:mm")