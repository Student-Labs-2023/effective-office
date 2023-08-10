package band.effective.office.tablet.ui.mainScreen.editBooking

import band.effective.office.tablet.ui.mainScreen.editBooking.store.EditBookingStore
import band.effective.office.tablet.ui.mainScreen.editBooking.store.EditBookingStoreFactory
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.component.KoinComponent

class EditBookingComponentImpl(
    componentContext: ComponentContext,
    storeFactory: StoreFactory, override val state: StateFlow<EditBookingStore.State>,
) : ComponentContext by componentContext, EditBookingComponent, KoinComponent {

    private val editBookingStore = instanceKeeper.getStore {
        EditBookingStoreFactory(storeFactory).create()
    }

    override fun onIntent(intent: EditBookingStore.Intent) {

    }
}