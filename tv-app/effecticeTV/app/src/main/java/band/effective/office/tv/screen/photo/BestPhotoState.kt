package band.effective.office.tv.screen.photo

import androidx.tv.foundation.lazy.list.TvLazyListState
import androidx.tv.foundation.lazy.list.rememberTvLazyListState
import band.effective.office.tv.domain.autoplay.model.AutoplayState
import band.effective.office.tv.domain.autoplay.model.NavigateRequests
import band.effective.office.tv.screen.navigation.Screen
import band.effective.office.tv.screen.photo.model.Photo


data class BestPhotoState(
    override val isError: Boolean = false,
    val photos: List<Photo> = listOf(),
    val error: String = "",
    override val isPlay: Boolean = true,
    override val isLoading: Boolean = true,
    override val isData: Boolean = false,
    override val screenName: Screen = Screen.BestPhoto,
    override var navigateRequest: NavigateRequests = NavigateRequests.Nowhere
) : AutoplayState {
    companion object {
        val Empty = BestPhotoState()
    }
}

sealed interface BestPhotoEvent {
    object OnClickPlayButton : BestPhotoEvent

    data class OnClickNextItem(val index: Int) : BestPhotoEvent

    data class OnClickPreviousItem(val index: Int) : BestPhotoEvent

    data class OnRequestSwitchScreen(val request: NavigateRequests): BestPhotoEvent
}

sealed interface BestPhotoEffect {
    data class ScrollToItem(val index: Int) : BestPhotoEffect

    data class ChangePlayState(val isPlay: Boolean) : BestPhotoEffect

    object ScrollToNextItem : BestPhotoEffect
}
