package band.effective.office.elevator.ui.booking.components.modals

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import band.effective.office.elevator.ExtendedThemeColors
import band.effective.office.elevator.MainRes
import band.effective.office.elevator.components.EffectiveButton
import band.effective.office.elevator.components.OutlinedPrimaryButton
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun BookingSuccess(onMain: () -> Unit, close: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.Top),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(shape = RoundedCornerShape(size = 16.dp))
            .background(color = ExtendedThemeColors.colors.whiteColor)
            .padding(start = 16.dp, top = 24.dp, end = 16.dp, bottom = 24.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(
                    resource = MainRes.strings.place_booked,
                ),
                style = MaterialTheme.typography.h6.merge(
                    other = TextStyle(
                        fontWeight = FontWeight(
                            weight = 500
                        )
                    )
                ),
                textAlign = TextAlign.Center
            )

            Text(
                text = stringResource(
                    resource = MainRes.strings.good_working_day,
                ),
                style = MaterialTheme.typography.body1.merge(
                    other = TextStyle(
                        color = ExtendedThemeColors.colors._66x
                    )
                ),
                textAlign = TextAlign.Center
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
            horizontalAlignment = Alignment.Start,
        ) {
            EffectiveButton(
                buttonText = stringResource(resource = MainRes.strings.move_to_main_from_booking),
                onClick = onMain,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedPrimaryButton(
                onClick = close,
                title = MainRes.strings.close_booking,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}