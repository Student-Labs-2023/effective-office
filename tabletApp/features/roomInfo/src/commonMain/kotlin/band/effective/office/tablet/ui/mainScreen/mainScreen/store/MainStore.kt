package band.effective.office.tablet.ui.mainScreen.mainScreen.store

import com.arkivanov.mvikotlin.core.store.Store

interface MainStore : Store<MainStore.Intent, MainStore.State, Nothing> {
    sealed interface Intent {
        object OnOpenFreeRoomModal : Intent
        object OnOpenTimePickerModal: Intent
        object OnFreeRoomIntent : Intent
        object CloseModal : Intent
        object OnOpenTimePickerModal: Intent
        object OnBookingCurrentRoomRequest : Intent
        object OnBookingOtherRoomRequest : Intent
        data class OnDisconnectChange(val newValue: Boolean) : Intent
    }

    data class State(
        val isLoad: Boolean,
        val isData: Boolean,
        val isError: Boolean,
        val showBookingModal: Boolean,
        val showFreeModal: Boolean,
        val isDisconnect: Boolean,
        val showTimePickerModal: Boolean
    ) {
        fun showModal() = showFreeModal || showBookingModal

        companion object {
            val defaultState =
                State(
                    isLoad = true,
                    isData = false,
                    isError = false,
                    showBookingModal = false,
                    showFreeModal = false,
                    isDisconnect = false,
                    showTimePickerModal = false
                )
        }
    }
}