package band.effective.office.tv.screen.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import band.effective.office.tv.screen.eventStory.models.*
import band.effective.office.tv.screen.message.component.OneMessageScreen
import band.effective.office.tv.ui.theme.IndependentColors
import coil.ImageLoader
import band.effective.office.tv.screen.duolingo.DuolingoScreen

@Composable
fun EventStoryScreenContent(
    eventsInfo: List<StoryModel>,
    currentStoryIndex: Int,
    storyProgress: Float = 1f,
    modifier: Modifier = Modifier,
    imageLoader: ImageLoader,
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
                progress = storyProgress
            )
            when (eventsInfo[currentStoryIndex].storyType) {
                StoryType.Employee -> {
                    val storyData = eventsInfo[currentStoryIndex]
                    StoryContent(
                        employeeInfo = storyData as EmployeeInfoUI,
                        onImageLoading = onImageLoading,
                        onImageLoaded = onImageLoaded,
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
                StoryType.Message -> {
                    val messageItem = eventsInfo[currentStoryIndex] as MessageInfo
                    OneMessageScreen(
                        modifier = modifier,
                        imageLoader = imageLoader,
                        message = messageItem.message
                    )
                }
            }

        }
    }
}
