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
import band.effective.office.tv.screen.duolingo.DuolingoScreen
import band.effective.office.tv.screen.eventStory.models.DuolingoUserInfo
import band.effective.office.tv.screen.eventStory.models.EmployeeInfoUI
import band.effective.office.tv.screen.eventStory.models.StoryModel
import band.effective.office.tv.screen.eventStory.models.StoryType
import band.effective.office.tv.ui.theme.IndependentColors

@Composable
fun EventStoryScreenContent(
    eventsInfo: List<StoryModel>,
    currentStoryIndex: Int,
    modifier: Modifier = Modifier,
    onImageLoading: () -> Unit,
    onImageLoaded: () -> Unit,
) {
    Surface(
        modifier = modifier, color = IndependentColors.StoryBackgroundGray
    ) {
        Column {
            StoryIndicator(
                countStories = eventsInfo.size,
                currentStoryIndex = currentStoryIndex,
                modifier = Modifier
                    .padding(32.dp)
                    .fillMaxWidth()
                    .height(8.dp),
            )
            when(eventsInfo[currentStoryIndex].storyType) {
                StoryType.Employee -> {
                    val storyData = eventsInfo[currentStoryIndex]
                    StoryContent(
                        employeeInfo = storyData as EmployeeInfoUI,
                        onImageLoading = onImageLoading,
                        onImageLoaded =  onImageLoaded,
                        Modifier
                            .fillMaxSize()
                            .padding(horizontal = 64.dp)
                    )
                }
                StoryType.Duolingo -> {
                    val duolingoItem = eventsInfo[currentStoryIndex] as DuolingoUserInfo
                    DuolingoScreen(
                        keySort = duolingoItem.keySort,
                        duolingoUser = duolingoItem.users
                    )
                }
            }

        }
    }
}