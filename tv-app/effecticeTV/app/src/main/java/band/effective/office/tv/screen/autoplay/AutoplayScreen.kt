package band.effective.office.tv.screen.autoplay

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import band.effective.office.tv.domain.autoplay.AutoplayableViewModel
import band.effective.office.tv.domain.autoplay.model.ScreenDescription
import band.effective.office.tv.screen.error.ErrorScreen
import band.effective.office.tv.screen.eventStory.EventStoryScreen
import band.effective.office.tv.screen.eventStory.EventStoryViewModel
import band.effective.office.tv.screen.leaderIdEvents.LeaderIdEventsScreen
import band.effective.office.tv.screen.leaderIdEvents.LeaderIdEventsViewModel
import band.effective.office.tv.screen.load.LoadScreen
import band.effective.office.tv.screen.message.secondaryMessage.SecondaryMessageScreen
import band.effective.office.tv.screen.message.secondaryMessage.SecondaryMessageViewModel
import band.effective.office.tv.screen.navigation.Screen
import band.effective.office.tv.screen.photo.BestPhotoScreen
import band.effective.office.tv.screen.photo.PhotoViewModel

@Composable
fun ViewModelToScreen(viewModel: ViewModel) = when (viewModel) {
    is LeaderIdEventsViewModel -> @Composable {
        LeaderIdEventsScreen(viewModel)
    }
    is PhotoViewModel -> @Composable {
        BestPhotoScreen(viewModel)
    }
    is SecondaryMessageViewModel-> @Composable {
        SecondaryMessageScreen(viewModel)
    }
    is EventStoryViewModel -> @Composable {
        EventStoryScreen(viewModel)
    }
    else -> {}
}

object UserSelect {
    var viewModels = mapOf<Screen, ViewModel>()
}

@Composable
fun AutoplayScreen(viewModel: AutoplayViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    val viewModels = UserSelect.viewModels
    viewModels.forEach { (screen, vm) ->
        viewModel.autoplayController.registerScreen(
            ScreenDescription(
                screenName = screen,
                screen = { ViewModelToScreen(vm) },
                viewModel = vm as AutoplayableViewModel
            )
        )
    }
    when {
        state.isLoading -> LoadScreen()
        state.isError -> ErrorScreen("error")
        state.isLoaded -> viewModel.getScreen(state.currentScreen).invoke()
    }
}