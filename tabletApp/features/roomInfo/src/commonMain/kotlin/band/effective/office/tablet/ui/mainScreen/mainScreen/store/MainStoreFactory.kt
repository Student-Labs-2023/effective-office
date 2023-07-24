package band.effective.office.tablet.ui.mainScreen.mainScreen.store

import band.effective.office.tablet.domain.CurrentEventController
import band.effective.office.tablet.domain.model.RoomInfo
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

class MainStoreFactory(private val storeFactory: StoreFactory) : KoinComponent {

    private val updateUseCase: UpdateUseCase by inject()
    private val currentEventController: CurrentEventController by inject()

    @OptIn(ExperimentalMviKotlinApi::class)
    fun create(): MainStore =
        object : MainStore,
            Store<MainStore.Intent, MainStore.State, Nothing> by storeFactory.create(
                name = "MainStore",
                initialState = MainStore.State.defaultState,
                bootstrapper = coroutineBootstrapper {
                    launch {
                        currentEventController.timeToUpdate.collect {
                            dispatch(
                                Action.UpdateChangeEventTime(
                                    (it / 60000).toInt()
                                )
                            )
                        }
                    }
                    launch() {
                        dispatch(Action.UpdateRoomInfo(updateUseCase.getRoomInfo()))
                        updateUseCase(
                            scope = this,
                            roomUpdateHandler = { roomInfo ->
                                launch(Dispatchers.Main.immediate) {
                                    dispatch(Action.UpdateRoomInfo(roomInfo))
                                }

                            },
                            organizerUpdateHandler = {})
                    }
                },
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl
            ) {}

    private sealed interface Action {
        data class UpdateRoomInfo(val roomInfo: RoomInfo) : Action
        data class UpdateChangeEventTime(val newValue: Int) : Action
    }

    private sealed interface Message {
        data class UpdateRoomInfo(val roomInfo: RoomInfo) : Message
        object BookingCurrentRoom : Message
        object BookingOtherRoom : Message
        object CloseModal : Message
        object OpenFreeModal : Message
        object StartFreeRoom : Message
        object FinishFreeRoom : Message
        data class UpdateChangeEventTime(val newValue: Int) : Message
    }

    private inner class ExecutorImpl() :
        CoroutineExecutor<MainStore.Intent, Action, MainStore.State, Message, Nothing>() {
        override fun executeIntent(intent: MainStore.Intent, getState: () -> MainStore.State) {
            when (intent) {
                is MainStore.Intent.OnBookingCurrentRoomRequest -> dispatch(Message.BookingCurrentRoom)
                is MainStore.Intent.OnBookingOtherRoomRequest -> dispatch(Message.BookingOtherRoom)
                is MainStore.Intent.CloseModal -> dispatch(Message.CloseModal)
                is MainStore.Intent.OnFreeRoomIntent -> freeRoom()
                is MainStore.Intent.OnOpenFreeRoomModal -> dispatch(Message.OpenFreeModal)
            }
        }

        override fun executeAction(action: Action, getState: () -> MainStore.State) {
            when (action) {
                is Action.UpdateRoomInfo -> dispatch(Message.UpdateRoomInfo(action.roomInfo))
                is Action.UpdateChangeEventTime -> dispatch(Message.UpdateChangeEventTime(action.newValue))
            }
        }

        private fun freeRoom() = scope.launch {
            dispatch(Message.StartFreeRoom)
            currentEventController.cancelCurrentEvent()
            dispatch(Message.FinishFreeRoom)
        }
    }

    private object ReducerImpl : Reducer<MainStore.State, Message> {
        override fun MainStore.State.reduce(message: Message): MainStore.State =
            when (message) {
                is Message.BookingCurrentRoom -> copy(showBookingModal = true)
                is Message.BookingOtherRoom -> copy()
                is Message.UpdateRoomInfo -> copy(
                    roomInfo = message.roomInfo,
                    isData = true,
                    isLoad = false
                )

                is Message.CloseModal -> copy(showBookingModal = false, showFreeModal = false)
                is Message.OpenFreeModal -> copy(showFreeModal = true)
                is Message.FinishFreeRoom -> copy(showFreeModal = false)
                is Message.StartFreeRoom -> copy()
                is Message.UpdateChangeEventTime -> copy(changeEventTime = message.newValue)
            }
    }
}