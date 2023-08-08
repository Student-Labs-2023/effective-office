package band.effective.office.tablet.ui.mainScreen.mainScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import band.effective.office.tablet.ui.mainScreen.mainScreen.store.MainStore
import band.effective.office.tablet.ui.mainScreen.mainScreen.uiComponents.ErrorMainScreen
import band.effective.office.tablet.ui.mainScreen.mainScreen.uiComponents.LoadMainScreen
import band.effective.office.tablet.ui.mainScreen.settingsComponents.SettingsView

@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
@Composable
fun MainScreen(component: MainComponent) {
    val state by component.state.collectAsState()
    when {
        state.isError -> {
            ErrorMainScreen { component.sendIntent(MainStore.Intent.RebootRequest) }
        }

        state.isLoad -> {
            LoadMainScreen()
        }

        state.isData -> {
            SettingsView()
           /* MainScreenView(
                showBookingModal = state.showBookingModal,
                showFreeRoomModal = state.showFreeModal,
                showDateTimePickerModal = state.showDateTimePickerModal,
                bookingRoomComponent = component.bookingRoomComponent,
                selectRoomComponent = component.selectRoomComponent,
                roomInfoComponent = component.roomInfoComponent,
                freeSelectRoomComponent = component.freeSelectRoomComponent,
                dateTimePickerComponent = component.dateTimePickerComponent,
                showModal = state.showModal(),
                isDisconnect = state.isDisconnect
            )*/
        }
    }
}
