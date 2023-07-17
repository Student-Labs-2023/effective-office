package band.effective.office.elevator.ui.profile.mainProfile.store

import band.effective.office.elevator.ui.models.User
import com.arkivanov.mvikotlin.core.store.Store

interface ProfileStore : Store<ProfileStore.Intent, User, ProfileStore.Label> {

    sealed interface Intent {
        object SignOutClicked : Intent
        object EditProfileClicked : Intent
    }

    sealed interface Label {
        object OnSignedOut : Label
        object OnClickedEdit: Label
    }
}