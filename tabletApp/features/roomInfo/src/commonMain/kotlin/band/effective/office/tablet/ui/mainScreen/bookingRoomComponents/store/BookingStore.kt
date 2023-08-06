package band.effective.office.tablet.ui.mainScreen.bookingRoomComponents.store

import band.effective.office.tablet.domain.model.EventInfo
import band.effective.office.tablet.domain.model.Organizer
import com.arkivanov.mvikotlin.core.store.Store
import java.util.Calendar
import java.util.GregorianCalendar

interface BookingStore : Store<BookingStore.Intent, BookingStore.State, BookingStore.Label> {
    sealed interface Intent {
        object OnBookingCurrentRoom : Intent
        object OnBookingOtherRoom : Intent
        data class OnChangeDate(val changeInDay: Int) : Intent
        data class OnChangeLength(val change: Int) : Intent
        data class OnChangeOrganizer(val newOrganizer: String) : Intent
        object OnChangeExpanded : Intent
        data class OnChangeIsActive(val reset: Boolean): Intent
    }

    sealed interface Label{
        object BookingCurrentRoom: Label
        object BookingOtherRoom: Label
        object ChangeDate: Label
    }

    data class State(
        val length: Int,
        val organizer: Organizer,
        val isOrganizerError: Boolean,
        val organizers: List<Organizer>,
        val selectDate: Calendar,
        val currentDate: Calendar,
        val isBusy: Boolean,
        val busyEvent: EventInfo,
        val roomName: String,
        val isExpandedOrganizersList: Boolean,
        val isSelectCurrentTime: Boolean,
        val isActive: Boolean
    ) {
        fun isCorrect() = isCorrectOrganizer() && isCorrectLength() && isCorrectDate()

        fun isCorrectOrganizer() = BookingInfoValidator.validateOrganizer(organizer)
        fun isCorrectLength() = BookingInfoValidator.validateLength(length)
        fun isCorrectDate() = BookingInfoValidator.validateDate(selectDate)

        companion object {
            val default = State(
                length = 30,
                organizer = Organizer.default,
                organizers = listOf(),
                selectDate = GregorianCalendar(),
                currentDate = GregorianCalendar(),
                isBusy = false,
                busyEvent = EventInfo.emptyEvent,
                roomName = "Sirius",
                isOrganizerError = false,
                isExpandedOrganizersList = false,
                isSelectCurrentTime = true,
                isActive = true
            )
        }
    }

    object BookingInfoValidator {
        fun validateDate(date: Calendar): Boolean {
            val date = date.clone() as Calendar
            date.add(Calendar.SECOND, 59)
            return date > GregorianCalendar()
        }

        fun validateLength(length: Int) = length > 0

        fun validateOrganizer(organizer: Organizer) = organizer.fullName != ""
    }
}