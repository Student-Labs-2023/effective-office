package band.effective.office.tablet.ui.mainScreen.roomInfoComponents.uiComponent

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import band.effective.office.tablet.domain.model.EventInfo
import band.effective.office.tablet.features.roomInfo.MainRes
import band.effective.office.tablet.ui.theme.LocalCustomColorsPalette
import band.effective.office.tablet.ui.theme.ScrollBarColor
import band.effective.office.tablet.ui.theme.h7
import band.effective.office.tablet.utils.CalendarStringConverter
import java.util.Calendar

@Composable
fun RoomEventListComponent(
    modifier: Modifier = Modifier,
    eventsList: List<EventInfo>,
    isToday: Boolean,
    onItemClick: (EventInfo) -> Unit
) {
    Column(modifier = modifier) {
        Text(
            text = "${MainRes.string.list_organizers_title} ${if (isToday) MainRes.string.on_today_string else ""}",
            style = MaterialTheme.typography.h7,
            color = LocalCustomColorsPalette.current.secondaryTextAndIcon
        )
        Spacer(modifier = Modifier.height(30.dp))
        Row {
            val lazyListState: LazyListState = rememberLazyListState()
            LazyColumn(
                modifier = Modifier.fillMaxWidth().simpleVerticalScrollbar(lazyListState),
                state = lazyListState
            ) {
                items(eventsList) { event ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .background(color = MaterialTheme.colors.surface, shape = RoundedCornerShape(10.dp))
                            .padding(10.dp)
                            .clickable { onItemClick(event) }) {
                        Text(
                            text = "${event.startTime.time()} - ${event.finishTime.time()}",
                            style = MaterialTheme.typography.h7,
                            color = LocalCustomColorsPalette.current.primaryTextAndIcon
                        )
                        Text(
                            text = " · ${event.organizer.fullName}",
                            style = MaterialTheme.typography.h7,
                            color = LocalCustomColorsPalette.current.secondaryTextAndIcon
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }

    }
}

private fun Calendar.time() = CalendarStringConverter.calendarToString(this, "HH:mm")

//NOTE(Maksim Mishenko): scrollBar: https://stackoverflow.com/questions/66341823/jetpack-compose-scrollbars
//drawRoundRect: https://semicolonspace.com/jetpack-compose-canvas-drawroundrect/
@Composable
fun Modifier.simpleVerticalScrollbar(
    state: LazyListState,
    width: Dp = 8.dp
): Modifier {
    return drawWithContent {
        drawContent()

        val firstVisibleElementIndex = state.layoutInfo.visibleItemsInfo.firstOrNull()?.index

        val elementHeight = this.size.height / state.layoutInfo.totalItemsCount
        val scrollbarOffsetY = (firstVisibleElementIndex ?: 0) * elementHeight
        val scrollbarHeight = state.layoutInfo.visibleItemsInfo.size * elementHeight
        val cornerRadius = CornerRadius(x = 36.dp.toPx(), y = 36.dp.toPx())

        if (scrollbarHeight < size.height) {
            drawRoundRect(
                color = ScrollBarColor.background,
                topLeft = Offset(this.size.width - width.toPx(), 0f),
                size = Size(width.toPx(), size.height),
                cornerRadius = cornerRadius
            )
            drawRoundRect(
                color = ScrollBarColor.pistonColor,
                topLeft = Offset(this.size.width - width.toPx(), scrollbarOffsetY),
                size = Size(width.toPx(), scrollbarHeight),
                cornerRadius = cornerRadius
            )
        }
    }
}