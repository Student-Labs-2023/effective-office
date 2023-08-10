package band.effective.office.tablet.ui.mainScreen.editBooking.store

import band.effective.office.tablet.domain.model.EventInfo
import com.arkivanov.mvikotlin.core.store.Store
import java.util.Calendar
import java.util.GregorianCalendar

interface EditBookingStore : Store<EditBookingStore.Intent, EditBookingStore.State, Nothing> {
    sealed interface Intent {
        data class OnChangeDate(val changeInDay: Int) : Intent
        data class OnChangeLength(val change: Int) : Intent
        data class OnChangeOrganizer(val newOrganizer: String) : Intent
        object OnChangeExpanded : Intent
        data class OnChangeIsActive(val reset: Boolean): Intent
        object CloseModal : Intent
    }

    data class State(
        val length: Int,
        val organizer: String,
        val isOrganizerError: Boolean,
        val organizers: List<String>,
        val selectDate: Calendar,
        val currentDate: Calendar,
        val isBusy: Boolean,
        val busyEvent: EventInfo,

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
                organizer = "",
                organizers = listOf(),
                selectDate = GregorianCalendar(),
                currentDate = GregorianCalendar(),
                isBusy = false,
                busyEvent = EventInfo.emptyEvent,
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

        fun validateOrganizer(organizer: String) = organizer != ""
    }
}