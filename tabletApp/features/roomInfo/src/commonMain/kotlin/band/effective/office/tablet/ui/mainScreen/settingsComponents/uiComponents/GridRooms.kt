package band.effective.office.tablet.ui.mainScreen.settingsComponents.uiComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import band.effective.office.tablet.ui.theme.LocalCustomColorsPalette

@Composable
fun GridRooms(
    modifier: Modifier
){
    val data = listOf("Pluto", "Sirius", "Moon", "Antares", "Sun")

    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        content = {
            items(data) { item ->
                CardRoom(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10))
                        .background(LocalCustomColorsPalette.current.elevationBackground)
                        .selectable(
                            selected = false,
                            onClick = { }
                        ),
                    nameRoom = item
                )
            }
        }
    )
}