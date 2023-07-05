package band.effective.office.tablet.ui.mainScreen.components.bookingRoomComponents.uiComponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import band.effective.office.tablet.domain.model.EventInfo
import band.effective.office.tablet.features.roomInfo.MainRes
import band.effective.office.tablet.utils.CalendarStringConverter
import io.github.skeptick.libres.compose.painterResource
import java.util.Calendar

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BusyAlertView(modifier: Modifier, event: EventInfo, onClick: () -> Unit) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier,
                painter = painterResource(MainRes.image.allert),
                contentDescription = null
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text = "Время ${event.startTime.time()} — ${event.finishTime.time()} занято · ${event.organizer}",
                color = Color(0xFFEB4C2A),
                fontSize = 19.sp
            )
        }
        Spacer(modifier = Modifier.height(30.dp))
        Surface(color = Color(0xFF252322), onClick = { onClick() }) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Смотреть свободные переговорки на это время",
                    color = Color(0xFFA362F8),
                    fontSize = 19.sp
                )
                Image(
                    modifier = Modifier,
                    painter = painterResource(MainRes.image.arrow_to_right),
                    contentDescription = null
                )
            }
        }

    }
}

private fun Calendar.time() = CalendarStringConverter.calendarToString(this, "HH:mm")