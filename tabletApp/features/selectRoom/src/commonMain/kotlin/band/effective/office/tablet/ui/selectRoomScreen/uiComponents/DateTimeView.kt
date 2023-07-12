package band.effective.office.tablet.ui.selectRoomScreen.uiComponents

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import band.effective.office.tablet.features.selectRoom.MainRes
import band.effective.office.tablet.domain.model.Booking
import band.effective.office.tablet.ui.theme.CustomDarkColors
import band.effective.office.tablet.ui.theme.LocalCustomColorsPalette
import band.effective.office.tablet.utils.date
import band.effective.office.tablet.utils.time24

@Composable
fun DateTimeView(modifier: Modifier, shape: RoundedCornerShape, booking: Booking) {
    Card(
        shape = shape,
        backgroundColor = LocalCustomColorsPalette.current.mountainBackground
    ) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Row {
                Text(
                    text = booking.eventInfo.startTime.date(),
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.colors.secondary
                )

                Spacer(modifier = Modifier.width(5.dp))

                Text(
                    text = MainRes.string.booking_time.format(
                        startTime = booking.eventInfo.startTime.time24(),
                        finishTime = booking.eventInfo.finishTime.time24()
                    ),
                    style = MaterialTheme.typography.h6,
                    color = LocalCustomColorsPalette.current.primaryTextAndIcon
                )
            }
        }

    }
}
