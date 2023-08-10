package band.effective.office.tablet.ui.mainScreen.editBooking.store

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

class EditBookingStoreFactory(private val storeFactory: StoreFactory) : KoinComponent {

    val checkBookingUseCase: CheckBookingUseCase by inject()
    val updateUseCase: UpdateUseCase by inject()
    val currentEventController: CurrentEventController by inject()

    @OptIn(ExperimentalMviKotlinApi::class)
    fun create(): EditBookingStore =
        object : EditBookingStore,
            Store<EditBookingStore.Intent, EditBookingStore.State, Nothing> by storeFactory.create(
                name = "MainStore",
                initialState = EditBookingStore.State.default,
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
        object BookingOtherRoom : Message
        object BookingCurrentRoom : Message

        data class UpdateOrganizers(val organizers: List<String>) : Message
        data class UpdateBusy(
            val isBusy: Boolean,
            val busyEvent: EventInfo
        ) : Message

        object Reset : Message
        object OnChangeExpanded : Message
        object UpdateTime : Message
        object OnChangeIsActive : Message
    }

    private inner class ExecutorImpl() :
        CoroutineExecutor<EditBookingStore.Intent, Action, EditBookingStore.State, Message, Nothing>() {
        override fun executeIntent(
            intent: EditBookingStore.Intent,
            getState: () -> EditBookingStore.State
        ) {
            when (intent) {
                is EditBookingStore.Intent.OnChangeDate -> changeDate(getState, intent.changeInDay)
                is EditBookingStore.Intent.OnChangeLength -> changeLength(getState, intent.change)
                is EditBookingStore.Intent.OnChangeOrganizer -> {
                    dispatch(Message.ChangeOrganizer(intent.newOrganizer))
                    reset(getState)
                }

                is EditBookingStore.Intent.OnChangeIsActive -> {
                    if (intent.reset) {
                        dispatch(Message.Reset)
                    }
                    dispatch(Message.OnChangeIsActive)
                    reset(getState)
                }

                EditBookingStore.Intent.OnChangeExpanded -> dispatch(Message.OnChangeExpanded)
                EditBookingStore.Intent.CloseModal -> {}
            }
        }

        var resetTimer: Job? = null

        fun reset(getState: () -> EditBookingStore.State) {
            resetTimer?.cancel()
            resetTimer = scope.launch {
                delay(60000)
                if (getState().isActive) {
                    dispatch(Message.Reset)
                }
            }
        }

        fun booking(isCurrentRoom: Boolean, state: EditBookingStore.State, booking: (() -> Unit)?) =
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

        override fun executeAction(action: Action, getState: () -> EditBookingStore.State) {
            when (action) {
                is Action.UpdateOrganizers -> dispatch(Message.UpdateOrganizers(action.organizers))
                is Action.Init -> {
                    val defaultEvent = EventInfo.emptyEvent
                    dispatch(
                        Message.ChangeEvent(
                            selectDate = defaultEvent.startTime,
                            length = EditBookingStore.State.default.length,
                            isSelectCurrentTime = EditBookingStore.State.default.isSelectCurrentTime
                        )
                    )
                }

                is Action.UpdateEvents -> reset(getState)
                Action.UpdateSelectTime -> dispatch(Message.UpdateTime)
            }
        }

        fun changeDate(getState: () -> EditBookingStore.State, changeDay: Int) = scope.launch() {
            val state = getState()
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
            reset(getState)
        }

        fun changeLength(getState: () -> EditBookingStore.State, change: Int) = scope.launch() {
            val state = getState()
            if (state.length + change <= 0) return@launch
            dispatch(
                Message.ChangeEvent(
                    selectDate = state.selectDate,
                    length = state.length + change,
                    isSelectCurrentTime = state.isSelectCurrentTime
                )
            )
            reset(getState)
        }
    }

    private fun EditBookingStore.State.toEvent(): EventInfo {
        val finishDate = selectDate.clone() as Calendar
        finishDate.add(Calendar.MINUTE, length)
        return EventInfo(
            startTime = selectDate.clone() as Calendar,
            finishTime = finishDate,
            organizer = organizer
        )
    }

    private object ReducerImpl : Reducer<EditBookingStore.State, Message> {
        override fun EditBookingStore.State.reduce(msg: Message): EditBookingStore.State =
            when (msg) {
                is Message.BookingOtherRoom -> copy(
                    isActive = false
                )

                is Message.BookingCurrentRoom -> copy(
                    isActive = false
                )

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
                is Message.OnChangeIsActive -> copy(isActive = true)
            }

        fun EditBookingStore.State.reset() = copy(
            organizer = EditBookingStore.State.default.organizer,
            selectDate = GregorianCalendar(),
            length = EditBookingStore.State.default.length,
            isSelectCurrentTime = EditBookingStore.State.default.isSelectCurrentTime,
            isOrganizerError = EditBookingStore.State.default.isOrganizerError
        )

    }
}

private fun Calendar.isNow(): Boolean {
    val difference = (this.time.time - GregorianCalendar().time.time).absoluteValue
    return difference <= 60000
}