package band.effective.office.tablet.ui.mainScreen.components.bookingRoomComponents.uiComponents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import band.effective.office.tablet.ui.mainScreen.components.bookingRoomComponents.RealEventLengthComponent

@Composable
fun EventLengthView(
    modifier: Modifier = Modifier,
    component: RealEventLengthComponent,
    currentLength: Int,
    isBusy: Boolean
) {
    val space = 50.dp
    Column(modifier = modifier) {
        Text(
            text = "на сколько",
            color = Color(0xFF808080),
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                modifier = Modifier.fillMaxHeight().weight(1f).clip(RoundedCornerShape(15.dp)),
                onClick = { component.decrement() },
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color(0xFFFFFFFF),
                    backgroundColor = Color(0xFF302D2C)
                )
            ) {
                Text(
                    text = "-30",
                    color = Color(0xFFFAFAFA),
                    fontSize = 20.sp
                )
            }
            Spacer(modifier = Modifier.width(space))
            Text(
                text = "$currentLength мин",
                color = if (isBusy) Color(0xFFA362F8) else Color(0xFFFAFAFA),
                fontSize = 32.sp
            )
            Spacer(modifier = Modifier.width(space))
            Button(
                modifier = Modifier.fillMaxHeight().weight(1f).clip(RoundedCornerShape(15.dp)),
                onClick = { component.increment() },
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color(0xFFFFFFFF),
                    backgroundColor = Color(0xFF302D2C)
                )
            ) {
                Text(
                    text = "+15",
                    color = Color(0xFFFAFAFA),
                    fontSize = 20.sp
                )
            }
        }
    }

}