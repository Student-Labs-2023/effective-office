package band.effective.office.tablet.ui.mainScreen.bookingRoomComponents.store

import band.effective.office.tablet.domain.model.EventInfo
import band.effective.office.tablet.domain.model.RoomInfo
import band.effective.office.tablet.domain.useCase.CheckBookingUseCase
import band.effective.office.tablet.domain.useCase.UpdateUseCase
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.ExperimentalMviKotlinApi
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.Calendar

class BookingStoreFactory(private val storeFactory: StoreFactory) : KoinComponent {

    val checkBookingUseCase: CheckBookingUseCase by inject()
    val updateUseCase: UpdateUseCase by inject()

    @OptIn(ExperimentalMviKotlinApi::class)
    fun create(): BookingStore =
        object : BookingStore,
            Store<BookingStore.Intent, BookingStore.State, Nothing> by storeFactory.create(
                name = "MainStore",
                initialState = BookingStore.State.default,
                bootstrapper = coroutineBootstrapper {
                    launch(Dispatchers.IO) {
                        val busyEvent = checkBookingUseCase(EventInfo.emptyEvent)
                        launch(Dispatchers.Main) {
                            dispatch(
                                Action.Init(
                                    organizers = updateUseCase.getOrganizersList(),
                                    isBusy = busyEvent != null,
                                    busyEvent = busyEvent ?: EventInfo.emptyEvent
                                )
                            )
                            dispatch(Action.UpdateOrganizers(updateUseCase.getOrganizersList()))
                            updateUseCase(scope = this,
                                 {
                                    launch(Dispatchers.Main) {
                                        dispatch(Action.UpdateEvents(it))
                                    }

                                },
                                {
                                    launch(Dispatchers.Main) {
                                        dispatch(Action.UpdateOrganizers(it))
                                    }
                                })
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
    }

    private sealed interface Message {
        data class ChangeEvent(
            val selectDate: Calendar,
            val length: Int,
            val isBusy: Boolean,
            val busyEvent: EventInfo
        ) : Message

        data class ChangeOrganizer(val newOrganizer: String) : Message
        object BookingCurrentRoom : Message
        object BookingOtherRoom : Message
        data class UpdateOrganizers(val organizers: List<String>) : Message
        data class UpdateBusy(
            val isBusy: Boolean,
            val busyEvent: EventInfo
        ) : Message
    }

    private inner class ExecutorImpl() :
        CoroutineExecutor<BookingStore.Intent, Action, BookingStore.State, Message, Nothing>() {
        override fun executeIntent(
            intent: BookingStore.Intent,
            getState: () -> BookingStore.State
        ) {
            when (intent) {
                is BookingStore.Intent.OnBookingCurrentRoom -> dispatch(Message.BookingCurrentRoom)
                is BookingStore.Intent.OnBookingOtherRoom -> dispatch(Message.BookingOtherRoom)
                is BookingStore.Intent.OnChangeDate -> changeDate(getState(), intent.changeInDay)
                is BookingStore.Intent.OnChangeTime -> changeTime(getState(), intent.changeInTimeMillis)
                is BookingStore.Intent.OnChangeLength -> changeLength(getState(), intent.change)
                is BookingStore.Intent.OnChangeOrganizer -> dispatch(Message.ChangeOrganizer(intent.newOrganizer))
                is BookingStore.Intent.OnSetDay ->  setDayValueInDate(getState(), intent.changedDay)
                is BookingStore.Intent.OnSetMonth -> setMonthValueInDate(getState(), intent.changedMonth)
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
                            length = 0,
                            isBusy = action.isBusy,
                            busyEvent = action.busyEvent
                        )
                    )
                }

                is Action.UpdateEvents -> checkBusy(getState())
            }
        }

        fun changeDate(state: BookingStore.State, changeDay: Int) = scope.launch() {
            val event = state.copy(selectDate = state.selectDate.dayUpdate(changeDay)).toEvent()
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
            val event = state.copy(length = state.length + change).toEvent()
            val busyEvent = checkBookingUseCase(event)
            dispatch(
                Message.ChangeEvent(
                    event.startTime,
                    state.length + change,
                    busyEvent != null,
                    busyEvent ?: EventInfo.emptyEvent
                )
            )
        }

        fun checkBusy(state: BookingStore.State) = scope.launch {
            val busyEvent = checkBookingUseCase(state.toEvent())
            dispatch(
                Message.UpdateBusy(
                    busyEvent != null,
                    busyEvent ?: EventInfo.emptyEvent
                )
            )
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
                is Message.BookingCurrentRoom -> copy()
                is Message.BookingOtherRoom -> copy()
                is Message.ChangeEvent -> copy(
                    selectDate = msg.selectDate,
                    length = msg.length,
                    isBusy = msg.isBusy,
                    busyEvent = msg.busyEvent
                )

                is Message.ChangeOrganizer -> copy(organizer = msg.newOrganizer)
                is Message.UpdateOrganizers -> copy(organizers = msg.organizers)
                is Message.UpdateBusy -> copy(isBusy = msg.isBusy, busyEvent = msg.busyEvent)
            }

    }

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
