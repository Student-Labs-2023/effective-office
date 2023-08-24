package band.effective.office.elevator.ui.booking.components.modals

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import band.effective.office.elevator.ExtendedThemeColors
import band.effective.office.elevator.MainRes
import band.effective.office.elevator.components.EffectiveButton
import band.effective.office.elevator.domain.models.BookingInfo
import band.effective.office.elevator.domain.models.BookingPeriod
import band.effective.office.elevator.textInBorderGray
import band.effective.office.elevator.ui.booking.models.Frequency
import band.effective.office.elevator.utils.MonthLocalizations
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun BookAccept(
    onClickCloseBookAccept: () -> Unit,
    confirmBooking: () -> Unit,
    bookingInfo: BookingInfo,
    frequency: Frequency,
    period: BookingPeriod
) {

    val startMonth = MonthLocalizations.getMonthName(
        month = bookingInfo.dateOfStart.month,
        locale = Locale(languageTag = Locale.current.language)
    )

    val finishMonth = MonthLocalizations.getMonthName(
        month = bookingInfo.dateOfEnd.month,
        locale = Locale(languageTag = Locale.current.language)
    )

    val startDay = bookingInfo.dateOfStart.dayOfMonth
    val finishDay = bookingInfo.dateOfEnd.dayOfMonth

    val startYear = bookingInfo.dateOfStart.year
    val finishYear = bookingInfo.dateOfEnd.year

    val startTime = bookingInfo.dateOfStart.time
    val finishTime = bookingInfo.dateOfEnd.time

    val date =
        if (startYear == finishYear) if (startMonth == finishMonth) if (startDay == finishDay) "$startDay $startMonth $startYear" else "$startDay - $finishDay $startMonth $startYear"
        else "$startDay $startMonth - $finishDay $finishMonth $startYear"
        else "$startDay $startMonth $startYear - $finishDay $finishMonth $finishYear"

    val time = "${startTime.hour.toString().padStart(2, '0')}:${
        startTime.minute.toString().padStart(2, '0')
    } - ${finishTime.hour.toString().padStart(2, '0')}:${
        finishTime.minute.toString().padStart(2, '0')
    }"

    Box {
        Column(modifier = Modifier.fillMaxWidth().background(Color.White)) {
            Spacer(modifier = Modifier.padding(vertical = 10.dp))
            Divider(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(fraction = .3f)
                    .height(4.dp)
                    .background(
                        color = ExtendedThemeColors.colors.dividerColor,
                        shape = RoundedCornerShape(size = 16.dp)
                    )
                    .padding(
                        bottom = 8.dp
                    )
            )

            Row(modifier = Modifier.padding(top = 10.dp, start = 16.dp, end = 16.dp)) {
                IconButton(
                    onClick = onClickCloseBookAccept,
                    modifier = Modifier
                        .align(Alignment.Top)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "Krestik",
                        tint = ExtendedThemeColors.colors.blackColor
                    )
                }
                Column(modifier = Modifier.padding(horizontal = 5.dp)) {
                    Text(
                        text = bookingInfo.seatName,
                        style = MaterialTheme.typography.subtitle1,
                        fontSize = 20.sp,
                        fontWeight = FontWeight(600),
                        color = ExtendedThemeColors.colors.blackColor,
                        modifier = Modifier.padding(top = 10.dp, bottom = 5.dp)
                    )
                    Text(
                        //                        text = when(frequency.getResearchEnd().first.first){
//                            "ThisDay" -> noPeriodReserve(bookingInfoDomain, frequency)
//                            "Never" -> noEndsPeriodReserve(bookingInfoDomain, frequency)
//                            "Date" ->  noEndsPeriodReserve(bookingInfoDomain, frequency)
//                            else ->  coupleTimesPeriodReserve(bookingInfoDomain, frequency)
//                        },
                        text = "${if (frequency.toString().isEmpty()) "$date $time" else "$frequency $time"}",
                        style = MaterialTheme.typography.subtitle1,
                        fontSize = 16.sp,
                        fontWeight = FontWeight(400),
                        color = textInBorderGray,
                        modifier = Modifier.padding(bottom = 27.dp)
                    )
                }
            }
            EffectiveButton(
                buttonText = stringResource(MainRes.strings.confirm_booking),
                onClick = confirmBooking,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            )
        }
    }
}

@Composable
fun coupleTimesPeriodReserve(bookingInfo: BookingInfo, frequency: Frequency):String{
    return when(frequency.getResearchEnd().third) {
        "Month" -> noEndsPeriodReserve(bookingInfo, frequency)
        else -> noPeriodReserve(bookingInfo, frequency)
    }
}

@Composable
fun noEndsPeriodReserve(bookingInfo: BookingInfo, frequency: Frequency):String{
    return when(frequency.getResearchEnd().third){
        "Day" -> stringResource(MainRes.strings.every_work_day) + " " + noPeriodReserve(bookingInfo, frequency)
        "Week" -> stringResource(MainRes.strings.every_week) + " " + noPeriodReserve(bookingInfo, frequency)
        "Month" -> stringResource(MainRes.strings.every_month) + " " + noPeriodReserve(bookingInfo, frequency)
        else -> stringResource(MainRes.strings.every_year) + " " + noPeriodReserve(bookingInfo, frequency)
    }
}

@Composable
fun noPeriodReserve(bookingInfo: BookingInfo, frequency: Frequency): String{
    return with(bookingInfo) {
        if(frequency.toString().isNotEmpty())"${frequency.toString()} " else {""} +
                if((frequency.getResearchEnd().third != "Week" && frequency.getResearchEnd().third != "Day") || frequency.getResearchEnd().first.first == "CoupleTimes"){
                "${dateOfStart.date.dayOfMonth} " +
                    if(frequency.getResearchEnd().third != "Month"){
                    dateOfStart.date.monthNumber}
                    else{""}
                }
                else{""}+
                " ${dateOfStart.time.hour.toString()}:${
            with(
                dateOfStart.time
            ) { if (minute.toString().length < 2) "0$minute" else minute.toString() }
        } - ${dateOfEnd.time.hour.toString()}:${
            with(
                dateOfEnd.time
            ) {
                if (minute.toString().length < 2) "0$minute" else minute.toString()
            }
        }"+ if(frequency.getResearchEnd().first.second.contains(".")){
                " \nпо " + frequency.getResearchEnd().first.second
            }else{
                if (frequency.getResearchEnd().first.second.isNotEmpty()){
                    " в ближайшие ${frequency.getResearchEnd().first.second} недель"
                }else ""
        }
    }
}