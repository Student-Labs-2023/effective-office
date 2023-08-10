package band.effective.office.tablet.ui.mainScreen.editBooking

import band.effective.office.tablet.ui.mainScreen.editBooking.store.EditBookingStore
import kotlinx.coroutines.flow.StateFlow

interface EditBookingComponent {
    val state: StateFlow<EditBookingStore.State>
    fun onIntent(intent: EditBookingStore.Intent)
}