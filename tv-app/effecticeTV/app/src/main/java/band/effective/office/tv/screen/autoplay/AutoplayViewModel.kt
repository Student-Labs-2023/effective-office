package band.effective.office.tv.screen.autoplay

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import band.effective.office.tv.core.ui.screen_with_controls.TimerSlideShow
import band.effective.office.tv.domain.autoplay.AutoplayController
import band.effective.office.tv.repository.leaderId.LeaderIdEventsInfoRepository
import band.effective.office.tv.repository.synology.SynologyRepository
import band.effective.office.tv.screen.leaderIdEvents.LeaderIdEventsScreen
import band.effective.office.tv.screen.leaderIdEvents.LeaderIdEventsViewModel
import band.effective.office.tv.screen.navigation.Screen
import band.effective.office.tv.screen.photo.BestPhotoScreen
import band.effective.office.tv.screen.photo.PhotoViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AutoplayViewModel @Inject constructor(
    synologyRepository: SynologyRepository,
    slideShow: TimerSlideShow,
    leaderIdEventsInfoRepository: LeaderIdEventsInfoRepository,
    val autoplayController: AutoplayController
) : ViewModel() {
    val photoViewModel: PhotoViewModel = PhotoViewModel(synologyRepository, slideShow)
    val eventsViewModel: LeaderIdEventsViewModel =
        LeaderIdEventsViewModel(leaderIdEventsInfoRepository, slideShow)

    private var mutableState = MutableStateFlow(AutoplayUiState.defaultState)
    val state = mutableState.asStateFlow()

    init {
        autoplayController.init(viewModelScope)
        autoplayController.registerScreen(
            ScreenDescription(
                Screen.Events, {
                    LeaderIdEventsScreen(eventsViewModel)
                }, eventsViewModel
            )
        )
        autoplayController.registerScreen(
            ScreenDescription(
                Screen.BestPhoto, {
                    BestPhotoScreen(
                        photoViewModel
                    )
                }, photoViewModel
            )
        )
        load()
    }

    fun load() = viewModelScope.launch {
        autoplayController.currentScreenIndex.collect{ curentScreenIndex ->
            if (curentScreenIndex<0) mutableState.update { it.copy(isLoading = true) }
            else mutableState.update { it.copy(isLoading = false, isLoaded = true, currentScreen = curentScreenIndex) }
        }
    }

    fun getScreen(screenIndex: Int) = autoplayController.screensList[screenIndex].screen
}