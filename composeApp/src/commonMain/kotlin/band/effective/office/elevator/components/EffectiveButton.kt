package band.effective.office.elevator.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp


//use this components for rectangular buttons with 40 dp corners with orange background
@Composable
fun EffectiveButton(
    buttonText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = MaterialTheme.colors.background
        ),
        modifier = modifier
            .clip(RoundedCornerShape(40.dp))
    ) {
        Text(
            text = buttonText,
            style = MaterialTheme.typography.button,
            modifier = Modifier.padding(vertical = 10.dp)
        )
    }
}