package band.effective.office.tv.screen.eventStory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import band.effective.office.tv.core.network.entity.Either
import band.effective.office.tv.core.ui.screen_with_controls.TimerSlideShow
import band.effective.office.tv.domain.autoplay.AutoplayableViewModel
import band.effective.office.tv.domain.autoplay.model.NavigateRequests
import band.effective.office.tv.domain.model.duolingo.DuolingoUser
import band.effective.office.tv.domain.model.notion.EmployeeInfoEntity
import band.effective.office.tv.domain.model.notion.processEmployeeInfo
import band.effective.office.tv.network.use_cases.DuolingoManager
import band.effective.office.tv.screen.duolingo.model.toUI
import band.effective.office.tv.screen.eventStory.models.DuolingoUserInfo
import band.effective.office.tv.screen.eventStory.models.StoryModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class EventStoryViewModel @Inject constructor(
    private val repository: EmployeeInfoRepository,
    private val timer: TimerSlideShow,
    private val duolingo: DuolingoManager
) :
    ViewModel(), AutoplayableViewModel {
    private val mutableState =
        MutableStateFlow(LatestEventInfoUiState.empty)
    override val state = mutableState.asStateFlow()

    override fun switchToFirstItem() {
        mutableState.update { state -> state.copy(currentStoryIndex = 0) }
    }

    override fun switchToLastItem() {
        mutableState.update { state -> state.copy(currentStoryIndex = state.eventsInfo.size - 1) }
    }

    init {

        viewModelScope.launch {
            initDataStory()
        }
        timer.init(
            scope = viewModelScope,
            callbackToEnd = {
                if (state.value.currentStoryIndex + 1 < state.value.eventsInfo.size) {
                    mutableState.update { it.copy(currentStoryIndex = it.currentStoryIndex + 1) }
                } else {
                    mutableState.update { it.copy(navigateRequest = NavigateRequests.Forward) }
                    mutableState.update { it.copy(currentStoryIndex = 0) }
                }
            },
            isPlay = state.value.isPlay
        )
        timer.startTimer()
    }


    private suspend fun initDataStory() {
        withContext(ioDispatcher) {
            repository.latestEvents.combine(duolingo.getDuolingoUserinfo()) { employeeInfoEntities: List<EmployeeInfoEntity>, usersDuolingo: Either<String, List<DuolingoUser>> ->
                when (usersDuolingo) {
                    is Either.Success -> {
                        employeeInfoEntities.processEmployeeInfo() +
                            run {
                                val users = usersDuolingo.data
                                val userXpSort = DuolingoUserInfo(
                                    users = users
                                        .sortedByDescending { it.totalXp }
                                        .subList(0, if (users.size <= 10) users.size else 11) // 11 because we show 10 users on screen
                                        .toUI(),
                                    keySort = KeySortDuolingoUser.Xp
                                    ) as StoryModel
                                val userStreakSort = DuolingoUserInfo(
                                    users = users
                                        .sortedByDescending { it.streakDay }
                                        .subList(0, if (users.size <= 10) users.size else 11) // 11 because we show 10 users on screen
                                        .filter { it.streakDay != 0 }
                                        .toUI(),
                                    keySort = KeySortDuolingoUser.Streak
                                    ) as StoryModel
                                listOf(userXpSort, userStreakSort)
                            }
                    }

                    is Either.Failure -> {
                        employeeInfoEntities.processEmployeeInfo()
                    }
                }
            }.catch { exception ->
                mutableState.update { state ->
                    state.copy(
                        isError = true,
                        errorText = exception.message ?: "",
                        isLoading = false,
                    )
                }
            }.collectLatest { events ->
                mutableState.update { oldState ->
                    oldState.copy(
                        isLoading = false,
                        isData = true,
                        isError = false,
                        eventsInfo = events,
                        currentStoryIndex = 0,
                        isPlay = true
                    )
                }
            }
    private fun updateStateAsException(error: Error) {
        mutableState.update { state ->
            state.copy(
                isError = true,
                errorText = error.message ?: "",
                isLoading = false,
            )
        }
    }

    private fun updateStateAsSuccessfulFetch(events: List<EmployeeInfoEntity>) {
        val resultList = events.processEmployeeInfo()
        mutableState.update { state ->
            state.copy(
                eventsInfo = resultList,
                currentStoryIndex = 0,
                isLoading = false,
                isData = true,
                isPlay = true,
            )
        }
    }

    fun sendEvent(event: EventStoryScreenEvents) {
        when (event) {
            is EventStoryScreenEvents.OnClickPlayButton -> {
                handlePlayState()
            }
            is EventStoryScreenEvents.OnClickNextItem -> {
                playNextStory()
            }
            is EventStoryScreenEvents.OnClickPreviousItem -> {
                playPreviousStory()
            }
        }
    }

    private fun handlePlayState() {
        timer.resetTimer()
        stopTimer()
        val isPlay = !state.value.isPlay
        mutableState.update { it.copy(isPlay = isPlay) }
        if (isPlay) timer.startTimer()
    }

    private fun playNextStory() {
        timer.resetTimer()
        if (isLastStory()) {
            onLastStory()
        } else {
            moveToNextStory()
        }
    }

    private fun playPreviousStory() {
        timer.resetTimer()
        if (isFirstStory()) {
            onFirstStory()
        } else {
            moveToPreviousStory()
        }
    }

    private fun moveToNextStory() =
        mutableState.update { it.copy(currentStoryIndex = it.currentStoryIndex + 1) }

    private fun moveToPreviousStory() =
        mutableState.update { it.copy(currentStoryIndex = it.currentStoryIndex - 1) }

    private fun isLastStory() = state.value.currentStoryIndex + 1 == state.value.eventsInfo.size

    private fun isFirstStory() = state.value.currentStoryIndex == 0

    private fun onLastStory() {
        mutableState.update { it.copy(navigateRequest = NavigateRequests.Forward) }
        mutableState.update { it.copy(currentStoryIndex = 0) }
    }

    private fun onFirstStory() {
        mutableState.update { it.copy(navigateRequest = NavigateRequests.Back) }
        mutableState.update { it.copy(currentStoryIndex = state.value.eventsInfo.size - 1) }
    }
}

