package band.effective.office.elevator.ui.main.store

import band.effective.office.elevator.domain.models.BookingInfo
import band.effective.office.elevator.ui.employee.aboutEmployee.models.BookingsFilter
import band.effective.office.elevator.ui.models.ElevatorState
import band.effective.office.elevator.ui.models.ReservedSeat
import com.arkivanov.mvikotlin.core.store.Store
import dev.icerock.moko.resources.StringResource
import kotlinx.datetime.LocalDate

interface MainStore : Store<MainStore.Intent, MainStore.State, MainStore.Label> {

    sealed interface Intent {
        object OnClickCallElevator : Intent

        data class OnClickShowOption(val bookingId: String) : Intent

        object OnClickOpenCalendar : Intent

        object OnClickCloseCalendar : Intent

        data class OnClickApplyDate(val date: LocalDate?) : Intent

        object OpenFiltersBottomDialog : Intent
        data class CloseFiltersBottomDialog(val bookingsFilter: BookingsFilter) : Intent

        object OnClickShowMap : Intent

        object OnClickDeleteBooking : Intent
    }

    sealed interface Label {
        data class ShowError(val errorState: ErrorState) : Label
        object ShowSuccess : Label

        object ShowOptions : Label

        object CloseOption : Label

        object CloseCalendar : Label

        object OpenCalendar : Label

        object OpenFiltersBottomDialog: Label
        object CloseFiltersBottomDialog: Label

        object OpenBooking: Label
        data class DeleteBooking(val id: String) : Label
    }

    data class State(
        val reservedSeats: List<ReservedSeat>,
        val elevatorState: ElevatorState,
        val currentDate: LocalDate,
        val dateFiltrationOnReserves: Boolean,
        val idSelectedBooking: String
    )

    data class ErrorState(val message: StringResource)
}