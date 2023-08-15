package band.effective.office.elevator.ui.booking.store

import band.effective.office.elevator.expects.showToast
import band.effective.office.elevator.ui.booking.models.WorkSpaceType
import band.effective.office.elevator.ui.booking.models.WorkSpaceUI
import band.effective.office.elevator.ui.booking.models.WorkSpaceZone
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.ExperimentalMviKotlinApi
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import org.koin.core.component.KoinComponent

class BookingStoreFactory(private val storeFactory: StoreFactory) : KoinComponent {

    @OptIn(ExperimentalMviKotlinApi::class)
    fun create(): BookingStore =
        object : BookingStore,
            Store<BookingStore.Intent, BookingStore.State, BookingStore.Label> by storeFactory.create(
                name = "BookingStore",
                initialState = BookingStore.State.initState,
                bootstrapper = coroutineBootstrapper {
                    launch {
                        dispatch(Action.InitWorkSpaces)
                    }
                },
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl
            ) {}

    private sealed interface Msg {
        data class BeginningBookingTime(val time: LocalTime) : Msg
        data class BeginningBookingDate(val date: LocalDate)
        data class EndBookingTime(val time: LocalTime) : Msg
        data class EndBookingDate(val date: LocalDate)
        data class TypeList(val type: String) : Msg
        data class DateBooking(val date: LocalDate) : Msg
        data class TimeBooking(val time: LocalTime) : Msg

        data class ChangeSelectedWorkSpacesZone(val workSpacesZone: List<WorkSpaceZone>) : Msg

        data class ChangeWorkSpacesUI(val workSpacesUI: List<WorkSpaceUI>) : Msg

        data class WholeDay(val wholeDay: Boolean) : Msg
        data class IsStartTimePicked(val isStart: Boolean) : Msg
    }

    private sealed interface Action {
        object InitWorkSpaces : Action
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<BookingStore.Intent, Action, BookingStore.State, Msg, BookingStore.Label>() {
        override fun executeIntent(
            intent: BookingStore.Intent,
            getState: () -> BookingStore.State
        ) {
            when (intent) {
                is BookingStore.Intent.ShowPlace -> dispatch(
                    Msg.TypeList(
                        type = intent.type
                    )
                )

                is BookingStore.Intent.OpenChooseZone -> {
                    scope.launch {
                        publish(BookingStore.Label.OpenChooseZone)
                    }
                }

                is BookingStore.Intent.CloseChooseZone -> {
                    scope.launch {
                        publish(BookingStore.Label.CloseChooseZone)
                    }
                }

                is BookingStore.Intent.OpenBookPeriod -> {
                    scope.launch {
                        publish(BookingStore.Label.OpenBookPeriod)
                    }
                }

                is BookingStore.Intent.CloseBookPeriod -> {
                    scope.launch {
                        publish(BookingStore.Label.CloseBookPeriod)
                    }
                }

                is BookingStore.Intent.OpenRepeatDialog -> {
                    scope.launch {
                        publish(BookingStore.Label.OpenRepeatDialog)
                    }
                }

                is BookingStore.Intent.OpenBookAccept -> {
                    scope.launch {
                        publish(BookingStore.Label.OpenBookAccept)
                    }
                }

                is BookingStore.Intent.CloseBookAccept -> {
                    scope.launch {
                        publish(BookingStore.Label.CloseBookAccept)
                    }
                }

                is BookingStore.Intent.OpenCalendar -> {
                    scope.launch {
                        publish(BookingStore.Label.OpenCalendar)
                    }
                }

                is BookingStore.Intent.CloseCalendar -> {
                    scope.launch {
                        publish(BookingStore.Label.CloseCalendar)
                    }
                }

                is BookingStore.Intent.ApplyDate -> {
                    scope.launch {
                        publish(BookingStore.Label.CloseCalendar)
                        intent.date?.let { newDate ->
                            dispatch(Msg.DateBooking(date = newDate))
                        }
                    }
                }

                is BookingStore.Intent.OpenConfirmBooking -> {
                    scope.launch {
                        publish(BookingStore.Label.CloseBookAccept)
                        publish(BookingStore.Label.OpenConfirmBooking)
                    }
                }

                is BookingStore.Intent.OpenMainScreen -> {
                    TODO()
                }

                is BookingStore.Intent.CloseConfirmBooking -> {
                    scope.launch {
                        publish(BookingStore.Label.CloseConfirmBooking)
                    }

                }

                is BookingStore.Intent.ApplyTime -> {
                    scope.launch {
                        if (intent.isStart) {
                            dispatch(Msg.BeginningBookingTime(intent.time))
                        } else {
                            dispatch(Msg.EndBookingTime(intent.time))
                        }
                        publish(BookingStore.Label.CloseStartTimeModal)
                    }
                }

                is BookingStore.Intent.CloseStartTimeModal -> {
                    scope.launch {
                        publish(BookingStore.Label.CloseStartTimeModal)
                    }

                }

                is BookingStore.Intent.OpenStartTimeModal -> {
                    scope.launch {
                        dispatch(Msg.IsStartTimePicked(isStart = intent.isStart))
                        publish(BookingStore.Label.OpenStartTimeModal)
                    }

                }

                is BookingStore.Intent.SearchSuitableOptions -> {
                    scope.launch {
                        publish(BookingStore.Label.CloseBookPeriod)
                    }

                }

                is BookingStore.Intent.OpenBookRepeat -> {
                    scope.launch {
                        publish(BookingStore.Label.CloseBookPeriod)
                        publish(BookingStore.Label.CloseRepeatDialog)
                        publish(BookingStore.Label.OpenBookRepeat)
                    }

                }

                is BookingStore.Intent.CloseBookRepeat -> {
                    scope.launch {
                        publish(BookingStore.Label.CloseBookRepeat)
                        publish(BookingStore.Label.OpenBookPeriod)
                    }
                }

                is BookingStore.Intent.ChangeSelectedWorkSpacesZone -> {
                    dispatch(Msg.ChangeSelectedWorkSpacesZone(intent.workSpaceZone))
                }

                is BookingStore.Intent.ChangeWorkSpacesUI -> {
                    intent.workSpaces.forEachIndexed { index1, workSpaceUI ->
                        getState().workSpacesZone.forEachIndexed { index2, workSpaceZone ->
                            intent.workSpaces.filter { workSpaceUI -> workSpaceUI.workSpaceName == workSpaceZone.name }
                        }
                    }
                    dispatch(Msg.ChangeWorkSpacesUI(workSpacesUI = intent.workSpaces))
                }

                is BookingStore.Intent.ChangeType -> {

                }

                is BookingStore.Intent.ChangeWholeDay -> {
                    dispatch(Msg.WholeDay(wholeDay = intent.wholeDay))
                }

                BookingStore.Intent.OpenFinishTimeModal -> {
                    scope.launch {
                        publish(BookingStore.Label.OpenFinishTimeModal)
                    }
                }

                BookingStore.Intent.CloseFinishTimeModal -> {
                    scope.launch {
                        publish(BookingStore.Label.CloseFinishTimeModal)
                    }

                }
            }
        }

        override fun executeAction(action: Action, getState: () -> BookingStore.State) =
            when (action) {
                Action.InitWorkSpaces -> initList(getState())
            }

        private fun initList(state: BookingStore.State) {
            scope.launch {
                dispatch(Msg.ChangeWorkSpacesUI(workSpacesUI = state.workSpaces.filter { workSpaceUI -> workSpaceUI.workSpaceType == WorkSpaceType.WORK_PLACE }))
            }
        }
    }

    private object ReducerImpl : Reducer<BookingStore.State, Msg> {
        override fun BookingStore.State.reduce(msg: Msg): BookingStore.State {
            return when (msg) {
                is Msg.ChangeSelectedWorkSpacesZone -> copy(workSpacesZone = msg.workSpacesZone)
                is Msg.DateBooking -> copy(selectedStartDate = msg.date)
                is Msg.TimeBooking -> copy(selectedStartTime = msg.time)
                is Msg.TypeList -> TODO()
                is Msg.ChangeWorkSpacesUI -> copy(workSpaces = msg.workSpacesUI)
                is Msg.WholeDay -> copy(wholeDay = msg.wholeDay)
                is Msg.BeginningBookingTime -> copy(selectedStartTime = msg.time)
                is Msg.EndBookingTime -> copy(selectedFinishTime = msg.time)
                is Msg.IsStartTimePicked -> copy(isStart = msg.isStart)
            }
        }
    }
}