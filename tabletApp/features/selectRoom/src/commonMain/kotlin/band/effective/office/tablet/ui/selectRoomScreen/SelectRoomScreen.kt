package band.effective.office.tablet.ui.selectRoomScreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import band.effective.office.tablet.domain.model.Booking
import band.effective.office.tablet.ui.selectRoomScreen.failureBooking.FailureSelectRoomView
import band.effective.office.tablet.ui.selectRoomScreen.store.SelectRoomStore
import band.effective.office.tablet.ui.selectRoomScreen.successBooking.SuccessSelectRoomView

@Composable
fun SelectRoomScreen(component: SelectRoomComponent) {
    val state by component.state.collectAsState()

    Dialog(
        onDismissRequest = { component.onIntent(SelectRoomStore.Intent.CloseModal) },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        component.onIntent(SelectRoomStore.Intent.SetBooking(Booking.default))
        when {
            state.error != null -> {
                FailureSelectRoomView(
                    onDismissRequest = { component.onIntent(SelectRoomStore.Intent.CloseModal) },
                    onClick = { component.onIntent(SelectRoomStore.Intent.BookingOtherRoom) })
            }

            state.isData -> {
                SelectRoomView(
                    booking = state.booking,
                    close = { component.onIntent(SelectRoomStore.Intent.CloseModal) },
                    bookRoom = { component.onIntent(SelectRoomStore.Intent.BookingRoom) },
                    isLoading = state.isLoading
                )
            }

            state.isSuccess -> {
                SuccessSelectRoomView(
                    booking = state.booking,
                    close = { component.onIntent(SelectRoomStore.Intent.CloseModal) }
                )
            }
        }
    }
}
