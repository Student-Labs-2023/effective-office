package band.effective.office.tablet.ui.selectRoomScreen.uiComponents

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import band.effective.office.tablet.features.selectRoom.MainRes
import band.effective.office.tablet.domain.model.Booking
import band.effective.office.tablet.ui.theme.CustomDarkColors
import band.effective.office.tablet.ui.theme.LocalCustomColorsPalette
import java.util.Calendar

@Composable
fun LengthEventView(modifier: Modifier, shape: RoundedCornerShape, booking: Booking) {

    val hours = getLengthEvent(booking.eventInfo.startTime, booking.eventInfo.finishTime) / 60
    val minutes = getLengthEvent(booking.eventInfo.startTime, booking.eventInfo.finishTime) % 60
    val lengthEvent: String = when(hours){
        0 -> MainRes.string.minutes.format( minutes = minutes.toString())
        else -> MainRes.string.hours_minutes.format(
            hours = hours.toString(),
            minutes = minutes.toString()
        )
    }

    Card(
        shape = shape,
        backgroundColor = LocalCustomColorsPalette.current.mountainBackground
    ) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = lengthEvent,
                style = MaterialTheme.typography.h6,
                color = LocalCustomColorsPalette.current.primaryTextAndIcon
            )
        }
    }
}

private fun getLengthEvent(start: Calendar, finish: Calendar) =
    (finish.get(Calendar.HOUR) * 60 + finish.get(Calendar.MINUTE)) -
            (start.get(Calendar.HOUR) * 60 + start.get(Calendar.MINUTE))