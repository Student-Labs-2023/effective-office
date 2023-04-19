package band.effective.office.tv.screen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import band.effective.office.tv.domain.models.Employee.EmployeeInfo
import band.effective.office.tv.screen.eventStory.StoryIndicator
import com.example.effecticetv.ui.theme.Gray
import com.example.effecticetv.ui.theme.LightGray

@Composable
fun EventInfo(eventsInfo: MutableList<EmployeeInfo>, currentStoryIndex: Int) {

    Surface(
        modifier = Modifier.fillMaxSize(), color = LightGray
    ) {
        Column() {
            StoryIndicator(
                eventsInfo, currentStoryIndex,
                Modifier
                    .padding(32.dp)
                    .fillMaxWidth()
                    .height(8.dp)
            )
            StoryContent(
                eventsInfo, currentStoryIndex,
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 64.dp)
            )
        }
    }
}
