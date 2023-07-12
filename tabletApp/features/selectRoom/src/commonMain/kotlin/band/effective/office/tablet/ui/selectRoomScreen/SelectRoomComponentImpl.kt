package band.effective.office.tablet.ui.selectRoomScreen

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import band.effective.office.tablet.domain.SelectRoomInteractor
import band.effective.office.tablet.domain.model.Booking

class SelectRoomComponentImpl(
    componentContext: ComponentContext,
    val booking: Booking,
    private val onCloseRequest: () -> Unit
) : ComponentContext by componentContext, SelectRoomComponent, KoinComponent {
    private val interactor: SelectRoomInteractor by inject()

    private var _state = MutableStateFlow(SelectRoomScreenState.defaultState)
    override val state = _state.asStateFlow()


    override fun bookRoom() {
    }

    fun close() {
        onCloseRequest()
    }

}