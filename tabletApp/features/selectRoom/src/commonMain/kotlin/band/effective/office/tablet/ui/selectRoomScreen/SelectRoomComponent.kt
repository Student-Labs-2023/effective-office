package band.effective.office.tablet.ui.selectRoomScreen

import kotlinx.coroutines.flow.StateFlow

interface SelectRoomComponent {
    val state: StateFlow<SelectRoomScreenState>
    fun bookRoom()
}