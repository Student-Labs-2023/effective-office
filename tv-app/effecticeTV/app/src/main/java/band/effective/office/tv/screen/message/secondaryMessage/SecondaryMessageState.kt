package band.effective.office.tv.screen.message.secondaryMessage

import band.effective.office.tv.domain.autoplay.model.AutoplayState
import band.effective.office.tv.domain.autoplay.model.NavigateRequests
import band.effective.office.tv.screen.navigation.Screen

data class SecondaryMessageState(
    val messageList: List<String>,
    override val isLoading: Boolean = false,
    override val isData: Boolean,
    override val isError: Boolean = false,
    override val screenName: Screen = Screen.MessageScreen,
    override var navigateRequest: NavigateRequests = NavigateRequests.Nowhere
) : AutoplayState {
    companion object {
        val empty = SecondaryMessageState(isData = true, messageList = listOf())
    }
}
