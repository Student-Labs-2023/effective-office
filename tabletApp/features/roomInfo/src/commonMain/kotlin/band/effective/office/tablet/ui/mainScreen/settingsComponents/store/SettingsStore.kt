package band.effective.office.tablet.ui.mainScreen.settingsComponents.store

import band.effective.office.tablet.domain.model.RoomsEnum
import com.arkivanov.mvikotlin.core.store.Store

interface SettingsStore: Store<SettingsStore.Intent, SettingsStore.State, Nothing> {
    sealed interface Intent{
        object OnExitApp: Intent
        data class SaveData(val nameRoom: String): Intent
    }

    data class State(
        val rooms: List<String>
    ) {
        companion object {
            val defaultState =
                State(
                    rooms = RoomsEnum.values().map { room -> room.nameRoom }.toList()
                )
        }
    }
}