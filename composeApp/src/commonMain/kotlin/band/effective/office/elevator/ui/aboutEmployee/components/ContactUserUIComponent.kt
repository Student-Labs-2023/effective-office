package band.effective.office.elevator.ui.aboutEmployee.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import band.effective.office.elevator.textInBorderPurple
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.compose.painterResource

@Composable
fun ContactUserUIComponent(image:ImageResource, value: String?, modifier: Modifier){
    Row(modifier = modifier){
       Icon(
           painter = painterResource(image),
           contentDescription = null,
           modifier = Modifier.size(16.dp),
           tint = textInBorderPurple
       )
        value?.let{
            Text(
                text = it,
                style = MaterialTheme.typography.caption,
                color = textInBorderPurple,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}