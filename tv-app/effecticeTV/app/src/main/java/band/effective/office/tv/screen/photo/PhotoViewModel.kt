package band.effective.office.tv.screen.photo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import band.effective.office.tv.BuildConfig
import band.effective.office.tv.core.ui.screen_with_controls.TimerSlideShow
import band.effective.office.tv.core.network.entity.Either
import band.effective.office.tv.domain.autoplay.AutoplayableViewModel
import band.effective.office.tv.domain.autoplay.model.AutoplayState
import band.effective.office.tv.network.UnsafeOkHttpClient
import band.effective.office.tv.repository.synology.SynologyRepository
import band.effective.office.tv.screen.photo.model.toUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import okhttp3.OkHttpClient
import javax.inject.Inject

@HiltViewModel
class PhotoViewModel @Inject constructor(
    private val repository: SynologyRepository,
    private val slideShow: TimerSlideShow,
    @UnsafeOkHttpClient val unsafeOkHttpClient: OkHttpClient
): ViewModel(), AutoplayableViewModel {

    private val mutableState = MutableStateFlow(BestPhotoState.Empty)
    override val state = mutableState.asStateFlow()

    private val mutableEffect = MutableSharedFlow<BestPhotoEffect>()
    val effect = mutableEffect.asSharedFlow()

    override fun switchToFirstItem(prevScreenState: AutoplayState) {
        if (prevScreenState.isPlay) slideShow.startTimer()
        mutableState.update { it.copy(isPlay = prevScreenState.isPlay) }
        viewModelScope.launch {
            mutableEffect.emit(BestPhotoEffect.ScrollToItem(0))
        }
    }

    override fun switchToLastItem(prevScreenState: AutoplayState) {
        if (prevScreenState.isPlay) slideShow.startTimer()
        mutableState.update { it.copy(isPlay = prevScreenState.isPlay) }
        viewModelScope.launch {
            mutableEffect.emit(BestPhotoEffect.ScrollToItem(state.value.photos.size-1))
        }
    }

    override fun stopTimer() {
        slideShow.stopTimer()
        mutableState.update { it.copy(isPlay = false) }
    }

    override fun startTimer() {
        slideShow.startTimer()
        mutableState.update { it.copy(isPlay = true) }
    }

    init {
        slideShow.init(
            scope = viewModelScope,
            callbackToEnd = { mutableEffect.emit(BestPhotoEffect.ScrollToNextItem) },
            isPlay = true
        )
        viewModelScope.launch {
            repository.auth()
            updatePhoto()
        }
    }

    fun sendEvent(event: BestPhotoEvent) {
        when (event) {
            is BestPhotoEvent.OnClickNextItem -> {
                slideShow.resetTimer()
                viewModelScope.launch {
                    mutableEffect.emit(BestPhotoEffect.ScrollToItem(event.index + 1))
                }
            }
            is BestPhotoEvent.OnClickPreviousItem -> {
                slideShow.resetTimer()
                viewModelScope.launch {
                    mutableEffect.emit(BestPhotoEffect.ScrollToItem(event.index - 1))
                }
            }
            BestPhotoEvent.OnClickPlayButton -> {
                val isPlay = !mutableState.value.isPlay
                mutableState.update {
                    it.copy(isPlay = isPlay)
                }
                if (!isPlay) slideShow.stopTimer()
                else {
                    slideShow.startTimer()
                }
                viewModelScope.launch {
                    mutableEffect.emit(BestPhotoEffect.ChangePlayState(isPlay))
                }
            }
            is BestPhotoEvent.OnRequestSwitchScreen ->{
                slideShow.stopTimer()
                mutableState.update { it.copy(navigateRequest = event.request) }
            }
        }
    }

    private suspend fun updatePhoto() {
        mutableState.update { state ->
            state.copy(isLoading = true)
        }

        repository.getPhotosUrl().collect { result ->
            when (result) {
                is Either.Failure -> {
                    mutableState.update { state ->
                        state.copy(isError = true, error = result.error, isLoading = false)
                    }
                }
                is Either.Success -> {
                    mutableState.update { state ->
                        state.copy(
                            photos = result.toUIModel(),
                            isError = false,
                            isLoading = false,
                            isData = true
                        )
                    }
                    slideShow.startTimer()
                }
            }
        }
    }
}
