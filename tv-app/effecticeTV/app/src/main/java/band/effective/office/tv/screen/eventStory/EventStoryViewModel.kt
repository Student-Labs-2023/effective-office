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
import band.effective.office.tv.repository.notion.EmployeeInfoRepository
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
) : ViewModel(), AutoplayableViewModel {
    private val mutableState = MutableStateFlow(LatestEventInfoUiState.empty)
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
        initTimer()
        startTimer()
    }

    private suspend fun initDataStory() {
        val exceptionHandler = CoroutineExceptionHandler { _, exception ->
            updateStateAsException((exception as Error).message.orEmpty())
        }
        withContext(Dispatchers.IO + exceptionHandler) {
            repository.latestEvents()
                .combine(duolingo.getDuolingoUserinfo()) { employeeInfoEntities: Either<String, List<EmployeeInfoEntity>>, usersDuolingo: Either<String, List<DuolingoUser>> ->
                    var error = ""
                    val duolingoInfo = when (usersDuolingo) {
                        is Either.Success -> {
                            run {
                                val users = usersDuolingo.data
                                val userXpSort = DuolingoUserInfo(
                                    users = users
                                        .sortedByDescending { it.totalXp }
                                        .subList(
                                            0,
                                            if (users.size <= countShowUsers) users.size else countShowUsers + 1
                                        )
                                        .toUI(),
                                    keySort = KeySortDuolingoUser.Xp
                                ) as StoryModel
                                val userStreakSort = DuolingoUserInfo(
                                    users = users
                                        .sortedByDescending { it.streakDay }
                                        .subList(
                                            0,
                                            if (users.size <= countShowUsers) users.size else countShowUsers + 1
                                        )
                                        .filter { it.streakDay != 0 }
                                        .toUI(),
                                    keySort = KeySortDuolingoUser.Streak
                                ) as StoryModel
                                listOf(userXpSort, userStreakSort)
                            }
                        }
                        is Either.Failure -> {
                            error = usersDuolingo.error
                            emptyList()
                        }
                    }
                    val notionInfo = when (employeeInfoEntities) {
                        is Either.Success -> {
                            employeeInfoEntities.data.processEmployeeInfo()
                        }
                        is Either.Failure -> {
                            error = employeeInfoEntities.error
                            emptyList()
                        }
                    }
                    if (notionInfo.isEmpty() && duolingoInfo.isEmpty()) return@combine Either.Failure(
                        error
                    )
                    else Either.Success(notionInfo + duolingoInfo)
                }.collectLatest { events ->
                when (events) {
                    is Either.Success -> updateStateAsSuccessfulFetch(events.data)
                    is Either.Failure -> updateStateAsException(events.error)
                }
            }
        }
    }

    private fun updateStateAsException(error: String) {
        mutableState.update { state ->
            state.copy(
                isError = true,
                errorText = error,
                isLoading = false,
            )
        }
    }

    private fun updateStateAsSuccessfulFetch(events: List<StoryModel>) {
        mutableState.update { state ->
            state.copy(
                eventsInfo = events,
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

    fun startTimer() {
        timer.startTimer()
    }

    fun stopTimer() {
        timer.stopTimer()
    }

    private fun initTimer() {
        timer.init(
            scope = viewModelScope,
            callbackToEnd = {
                if (isLastStory()) {
                    onLastStory()
                } else {
                    moveToNextStory()
                }
            },
            isPlay = state.value.isPlay
        )
    }

    private val countShowUsers = 10
}

