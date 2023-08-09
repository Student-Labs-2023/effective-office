package band.effective.office.elevator.ui.booking.components.modals

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ButtonElevation
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import band.effective.office.elevator.common.compose.components.GrayText

@Composable
fun TimeLine(
    date: String,
    statTime: String,
    endTime: String,
    selectTimeActive: Boolean,
    onPickDate: () -> Unit,
    onPickStartTime: () -> Unit,
    onPickEndTime: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 30.dp, end = 16.dp, top = 16.dp, bottom = 16.dp)
    ) {

        Text(
            text = date,
            modifier = Modifier.clickable { onPickDate() },
            style = MaterialTheme.typography.button.copy(
                fontWeight = FontWeight(
                    weight = 400
                ),
                color = Color.Black
            )
        )

        Spacer(modifier = Modifier.width(24.dp))

        if (selectTimeActive) {
            Text(
                text = statTime,
                modifier = Modifier.clickable { if (selectTimeActive) onPickStartTime() },
                style = MaterialTheme.typography.button.copy(
                    fontWeight = FontWeight(
                        weight = 400
                    ),
                    color = Color.Black
                )
            )
        } else {
            GrayText(
                text = statTime,
                style = MaterialTheme.typography.button.copy(
                    fontWeight = FontWeight(
                        weight = 400
                    ),
                    color = Color.Black
                )
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Divider(
            thickness = 1.dp,
            modifier = Modifier.width(11.dp),
            color = Color.Black
        )

        Spacer(modifier = Modifier.width(12.dp))

        if (selectTimeActive) {
            Text(
                text = endTime,
                modifier = Modifier.clickable { if (selectTimeActive) onPickEndTime() },
                style = MaterialTheme.typography.button.copy(
                    fontWeight = FontWeight(
                        weight = 400
                    ),
                    color = Color.Black
                )
            )
        } else {
            GrayText(
                text = endTime,
                style = MaterialTheme.typography.button.copy(
                    fontWeight = FontWeight(
                        weight = 400
                    ),
                    color = Color.Black
                )
            )
        }
    }
}