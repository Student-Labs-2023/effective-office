package band.effective.office.tablet.ui.mainScreen.mainScreen.store

import com.arkivanov.mvikotlin.core.store.Store

interface MainStore : Store<MainStore.Intent, MainStore.State, Nothing> {
    sealed interface Intent {
        object OnOpenFreeRoomModal : Intent
        object CloseModal : Intent
        object OnOpenDateTimePickerModal : Intent
        object OnBookingCurrentRoomRequest : Intent
        object OnBookingOtherRoomRequest : Intent
        data class OnDisconnectChange(val newValue: Boolean) : Intent
        object RebootRequest: Intent
    }

    data class State(
        val isLoad: Boolean,
        val isData: Boolean,
        val isError: Boolean,
        val showBookingModal: Boolean,
        val showFreeModal: Boolean,
        val showDateTimePickerModal: Boolean,
        val isDisconnect: Boolean,
        val isSettings: Boolean
    ) {
        fun showModal() = showFreeModal || showBookingModal || showDateTimePickerModal

        companion object {
            val defaultState =
                State(
                    isLoad = true,
                    isData = false,
                    isError = false,
                    showBookingModal = false,
                    showFreeModal = false,
                    showDateTimePickerModal = false,
                    isDisconnect = false,
                    isSettings = false
                )
        }
    }
}