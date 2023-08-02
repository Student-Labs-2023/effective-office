package band.effective.office.tablet.ui.mainScreen.bookingRoomComponents.store

import band.effective.office.tablet.domain.CurrentEventController
import band.effective.office.tablet.domain.model.EventInfo
import band.effective.office.tablet.domain.model.RoomInfo
import band.effective.office.tablet.domain.useCase.CheckBookingUseCase
import band.effective.office.tablet.domain.useCase.UpdateUseCase
import band.effective.office.tablet.utils.unbox
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.ExperimentalMviKotlinApi
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.Calendar
import java.util.GregorianCalendar
import kotlin.math.absoluteValue

class BookingStoreFactory(private val storeFactory: StoreFactory) : KoinComponent {

    val checkBookingUseCase: CheckBookingUseCase by inject()
    val updateUseCase: UpdateUseCase by inject()
    val currentEventController: CurrentEventController by inject()

    @OptIn(ExperimentalMviKotlinApi::class)
    fun create(): BookingStore =
        object : BookingStore,
            Store<BookingStore.Intent, BookingStore.State, Nothing> by storeFactory.create(
                name = "MainStore",
                initialState = BookingStore.State.default,
                bootstrapper = coroutineBootstrapper {
                    launch(Dispatchers.IO) {
                        val busyEvent =
                            checkBookingUseCase(EventInfo.emptyEvent).unbox({ it.saveData })
                        launch(Dispatchers.Main) {
                            dispatch(
                                Action.Init(
                                    organizers = updateUseCase.getOrganizersList()
                                        .unbox({ it.saveData ?: listOf() }),
                                    isBusy = busyEvent != null,
                                    busyEvent = busyEvent ?: EventInfo.emptyEvent
                                )
                            )
                            dispatch(
                                Action.UpdateOrganizers(
                                    updateUseCase.getOrganizersList()
                                        .unbox({ it.saveData ?: listOf() })
                                )
                            )
                            updateUseCase(scope = this,
                                {
                                    launch(Dispatchers.Main) {
                                        dispatch(Action.UpdateEvents(it.unbox({
                                            it.saveData ?: RoomInfo.defaultValue
                                        })))
                                    }

                                },
                                {
                                    launch(Dispatchers.Main) {
                                        dispatch(Action.UpdateOrganizers(it.unbox({
                                            it.saveData ?: listOf()
                                        })))
                                    }
                                })
                        }
                        launch(Dispatchers.Main) {
                            currentEventController.timeToUpdate.collect { dispatch(Action.UpdateSelectTime) }
                        }
                    }
                },
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl
            ) {}

    private sealed interface Action {
        data class UpdateOrganizers(val organizers: List<String>) : Action
        data class Init(
            val organizers: List<String>,
            val isBusy: Boolean,
            val busyEvent: EventInfo
        ) : Action

        data class UpdateEvents(val newData: RoomInfo) : Action
        object UpdateSelectTime : Action
    }

    private sealed interface Message {
        data class ChangeEvent(
            val selectDate: Calendar,
            val length: Int,
            val isSelectCurrentTime: Boolean
        ) : Message

        data class NotCorrectEvent(val busyEvent: EventInfo) : Message

        data class ChangeOrganizer(val newOrganizer: String) : Message
        object OrganizerError : Message

        data class UpdateOrganizers(val organizers: List<String>) : Message
        data class UpdateBusy(
            val isBusy: Boolean,
            val busyEvent: EventInfo
        ) : Message

        object Reset : Message
        object OnChangeExpanded : Message
        object UpdateTime : Message
    }

    private inner class ExecutorImpl() :
        CoroutineExecutor<BookingStore.Intent, Action, BookingStore.State, Message, Nothing>() {
        override fun executeIntent(
            intent: BookingStore.Intent,
            getState: () -> BookingStore.State
        ) {
            when (intent) {
                is BookingStore.Intent.OnBookingCurrentRoom -> booking(
                    isCurrentRoom = true,
                    state = getState(),
                    booking = intent.booking
                )

                is BookingStore.Intent.OnBookingOtherRoom -> booking(
                    isCurrentRoom = false,
                    state = getState(),
                    booking = intent.booking
                )

                is BookingStore.Intent.OnChangeDate -> changeDate(getState(), intent.changeInDay)
                is BookingStore.Intent.OnChangeTime -> changeTime(getState(), intent.changeInTimeMillis)
                is BookingStore.Intent.OnChangeLength -> changeLength(getState(), intent.change)
                is BookingStore.Intent.OnSetDay ->  setDayValueInDate(getState(), intent.changedDay)
                is BookingStore.Intent.OnSetMonth -> setMonthValueInDate(getState(), intent.changedMonth)
                is BookingStore.Intent.OnChangeOrganizer -> {
                    dispatch(Message.ChangeOrganizer(intent.newOrganizer))
                    reset()
                }

                BookingStore.Intent.OnChangeExpanded -> dispatch(Message.OnChangeExpanded)
            }
        }

        var resetTimer: Job? = null

        fun reset() {
            resetTimer?.cancel()
            resetTimer = scope.launch {
                delay(60000)
                dispatch(Message.Reset)
            }
        }

        fun booking(isCurrentRoom: Boolean, state: BookingStore.State, booking: (() -> Unit)?) =
            scope.launch {
                val busyEvent = checkBookingUseCase(state.toEvent()).unbox({ it.saveData })
                when {
                    !state.isCorrectOrganizer() -> dispatch(Message.OrganizerError)
                    isCurrentRoom && busyEvent != null -> dispatch(Message.NotCorrectEvent(busyEvent))
                    isCurrentRoom -> {
                        booking?.invoke()
                    }

                    else -> {
                        booking?.invoke()
                    }
                }
            }

        override fun executeAction(action: Action, getState: () -> BookingStore.State) {
            when (action) {
                is Action.UpdateOrganizers -> dispatch(Message.UpdateOrganizers(action.organizers))
                is Action.Init -> {
                    val defaultEvent = EventInfo.emptyEvent
                    dispatch(
                        Message.ChangeEvent(
                            selectDate = defaultEvent.startTime,
                            length = BookingStore.State.default.length,
                            isSelectCurrentTime = BookingStore.State.default.isSelectCurrentTime
                        )
                    )
                }

                is Action.UpdateEvents -> reset()
                Action.UpdateSelectTime -> dispatch(Message.UpdateTime)
            }
        }

        fun changeDate(state: BookingStore.State, changeDay: Int) = scope.launch() {
            val newDate = (state.selectDate.clone() as Calendar).apply {
                add(
                    Calendar.DAY_OF_MONTH,
                    changeDay
                )
            }
            dispatch(
                Message.ChangeEvent(
                    selectDate = newDate,
                    length = state.length,
                    isSelectCurrentTime = newDate.isNow()
                )
            )
            reset()
        }

        fun setDayValueInDate(state: BookingStore.State, newDay: Int) = scope.launch() {
            state.selectDate.set(Calendar.DAY_OF_MONTH, newDay)
            val event = state.copy(selectDate = state.selectDate).toEvent()
            val busyEvent = checkBookingUseCase(event)
            dispatch(
                Message.ChangeEvent(
                    event.startTime,
                    state.length,
                    busyEvent != null,
                    busyEvent ?: EventInfo.emptyEvent
                )
            )
        }

        fun setMonthValueInDate(state: BookingStore.State, newMonth: Int) = scope.launch() {
            state.selectDate.set(Calendar.MONTH, newMonth)
            val event = state.copy(selectDate =  state.selectDate).toEvent()
            val busyEvent = checkBookingUseCase(event)
            dispatch(
                Message.ChangeEvent(
                    event.startTime,
                    state.length,
                    busyEvent != null,
                    busyEvent ?: EventInfo.emptyEvent
                )
            )
        }

        fun changeTime(state: BookingStore.State, changeInTimeMillis: Long) = scope.launch() {
            val event = state.copy(selectDate = state.selectDate.setTime(changeInTimeMillis)).toEvent()
            val busyEvent = checkBookingUseCase(event)
            dispatch(
                Message.ChangeEvent(
                    event.startTime,
                    state.length,
                    busyEvent != null,
                    busyEvent ?: EventInfo.emptyEvent
                )
            )
        }

        fun changeLength(state: BookingStore.State, change: Int) = scope.launch() {
            if (state.length + change <= 0) return@launch
            dispatch(
                Message.ChangeEvent(
                    selectDate = state.selectDate,
                    length = state.length + change,
                    isSelectCurrentTime = state.isSelectCurrentTime
                )
            )
            reset()
        }
    }

    private fun BookingStore.State.toEvent(): EventInfo {
        val finishDate = selectDate.clone() as Calendar
        finishDate.add(Calendar.MINUTE, length)
        return EventInfo(
            startTime = selectDate.clone() as Calendar,
            finishTime = finishDate,
            organizer = organizer
        )
    }

    private object ReducerImpl : Reducer<BookingStore.State, Message> {
        override fun BookingStore.State.reduce(msg: Message): BookingStore.State =
            when (msg) {
                is Message.ChangeEvent -> copy(
                    selectDate = msg.selectDate,
                    length = msg.length,
                    isSelectCurrentTime = msg.isSelectCurrentTime,
                    isBusy = false
                )

                is Message.ChangeOrganizer -> copy(
                    organizer = msg.newOrganizer,
                    isOrganizerError = false,
                )

                is Message.UpdateOrganizers -> copy(organizers = msg.organizers)
                is Message.UpdateBusy -> copy(isBusy = msg.isBusy, busyEvent = msg.busyEvent)
                is Message.NotCorrectEvent -> copy(isBusy = true, busyEvent = msg.busyEvent)
                is Message.OrganizerError -> copy(isOrganizerError = true)
                is Message.Reset -> reset()
                is Message.OnChangeExpanded -> copy(isExpandedOrganizersList = !isExpandedOrganizersList)
                is Message.UpdateTime -> copy(currentDate = GregorianCalendar())
            }

        fun BookingStore.State.reset() = copy(
            organizer = BookingStore.State.default.organizer,
            selectDate = GregorianCalendar(),
            length = BookingStore.State.default.length,
            isSelectCurrentTime = BookingStore.State.default.isSelectCurrentTime,
            isOrganizerError = BookingStore.State.default.isOrganizerError
        )

    }
}

private fun Calendar.isNow(): Boolean {
    val difference = (this.time.time - GregorianCalendar().time.time).absoluteValue
    return difference <= 60000
    private fun Calendar.dayUpdate(changeDay: Int): Calendar {
        val result = clone() as Calendar
        result.add(Calendar.DAY_OF_MONTH, changeDay)
        return result
    }

    private fun Calendar.setTime(changeInTimeMillis: Long): Calendar {
        val result = clone() as Calendar
        result.timeInMillis += changeInTimeMillis
        return result
    }
}
