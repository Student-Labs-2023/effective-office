package band.effective.office.elevator.ui.booking

import band.effective.office.elevator.ui.booking.store.BookingStore
import band.effective.office.elevator.ui.booking.store.BookingStoreFactory
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow

class BookingComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
) :
    ComponentContext by componentContext {

    private val bookingStore = instanceKeeper.getStore {
        BookingStoreFactory(
            storeFactory = storeFactory
        ).create()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val state: StateFlow<BookingStore.State> = bookingStore.stateFlow
    fun onEvent(event: BookingStore.Intent) {
        bookingStore.accept(event)
    }
}