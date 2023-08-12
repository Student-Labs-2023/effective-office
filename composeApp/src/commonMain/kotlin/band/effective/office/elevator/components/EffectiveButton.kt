package band.effective.office.elevator.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun EffectiveButton(
    buttonText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: Dp = 14.dp,
    roundedCorner: Dp = 40.dp
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = MaterialTheme.colors.background
        ),
        contentPadding = PaddingValues(contentPadding),
        elevation = Elevation(),
        shape = RoundedCornerShape(roundedCorner),
        modifier = modifier
    ) {
        Text(
            text = buttonText,
            style = MaterialTheme.typography.button,
        )
    }
}