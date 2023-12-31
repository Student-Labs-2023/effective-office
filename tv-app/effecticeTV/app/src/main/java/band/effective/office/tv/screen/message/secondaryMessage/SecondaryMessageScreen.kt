package band.effective.office.tv.screen.message.secondaryMessage

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import band.effective.office.tv.screen.message.MessageScreen

@Composable
fun SecondaryMessageScreen(viewModel: SecondaryMessageViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    MessageScreen(
        modifier = Modifier.fillMaxSize(),
        imageLoader = viewModel.imageLoader,
        messagesList = state.messageList,
        currentIndex = state.currentIndex,
        uselessFact = state.uselessFact,
        isPlay = state.isPlay,
        onClickPlayButton = { viewModel.sendEvent(SecondaryMessageScreenEvents.OnClickPlayButton) },
        onClickNextItemButton = { viewModel.sendEvent(SecondaryMessageScreenEvents.OnClickNextButton) },
        onClickPreviousItemButton = { viewModel.sendEvent(SecondaryMessageScreenEvents.OnClickPrevButton) }
    )
}