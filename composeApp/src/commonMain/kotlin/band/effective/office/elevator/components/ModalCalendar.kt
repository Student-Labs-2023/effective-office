package band.effective.office.elevator.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import band.effective.office.elevator.MainRes
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.compose.stringResource
import epicarchitect.calendar.compose.basis.EpicMonth
import epicarchitect.calendar.compose.basis.config.rememberMutableBasisEpicCalendarConfig
import epicarchitect.calendar.compose.datepicker.config.rememberEpicDatePickerConfig
import epicarchitect.calendar.compose.datepicker.state.rememberEpicDatePickerState
import epicarchitect.calendar.compose.pager.config.rememberEpicCalendarPagerConfig
import kotlinx.datetime.LocalDate

@Composable
fun ModalCalendar(
    currentDate: LocalDate,
    onClickOk: (LocalDate?) -> Unit,
    onClickCansel: () -> Unit,
    modifier: Modifier = Modifier
) {
    val calendarConfig = rememberMutableBasisEpicCalendarConfig(
        contentColor = Color.Black
    )

    val state = rememberEpicDatePickerState(
        config = rememberEpicDatePickerConfig(
            pagerConfig = rememberEpicCalendarPagerConfig(
                basisConfig = calendarConfig
            ),
            selectionContentColor = MaterialTheme.colors.onPrimary,
            selectionContainerColor = MaterialTheme.colors.secondaryVariant,
        ),
        initialMonth = EpicMonth(year = currentDate.year, month = currentDate.month),
        selectedDates = listOf(currentDate)
    )

    Column(
        modifier = modifier
            .background(Color.White, shape = RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ){
        Calendar(state = state)
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
           ButtonAction(
               onClick = onClickCansel,
               buttonText = MainRes.strings.cansel
           )
            Spacer(modifier = Modifier.width(14.dp))
            ButtonAction(
                onClick = { onClickOk(state.selectedDates.firstOrNull()) },
                buttonText = MainRes.strings.ok
            )
        }
    }
}

@Composable
private fun ButtonAction(
    onClick: () -> Unit,
    buttonText: StringResource
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.White,
            contentColor = Color.Black
        ),
        elevation = ButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp)
    ) {
        Text(
            text = stringResource(buttonText),
            color = Color.Black,
            fontSize = 16.sp
        )
    }
}