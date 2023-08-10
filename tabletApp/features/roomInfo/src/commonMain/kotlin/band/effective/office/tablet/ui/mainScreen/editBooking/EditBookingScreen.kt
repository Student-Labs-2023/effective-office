package band.effective.office.tablet.ui.mainScreen.editBooking

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import band.effective.office.tablet.ui.mainScreen.editBooking.store.EditBookingStore

@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
@Composable
fun EditBookingScreen(component: EditBookingComponent) {
    val state by component.state.collectAsState()

    Dialog(
        onDismissRequest = { component.onIntent(EditBookingStore.Intent.CloseModal) },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        EditBookingView(editBookingComponent = component)
    }
}