package band.effective.office.tv.screen.autoplay

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import band.effective.office.tv.domain.autoplay.AutoplayController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AutoplayViewModel @Inject constructor(val autoplayController: AutoplayController) : ViewModel() {
    private var mutableState = MutableStateFlow(AutoplayUiState.defaultState)
    val state = mutableState.asStateFlow()

    init {
        autoplayController.init(viewModelScope)
        load()
    }

    fun load() = viewModelScope.launch {
        autoplayController.currentScreenIndex.collect { currentScreenIndex ->
            if (currentScreenIndex < 0) mutableState.update { it.copy(isLoading = true) }
            else mutableState.update {
                it.copy(
                    isLoading = false,
                    isLoaded = true,
                    currentScreen = currentScreenIndex
                )
            }
        }
    }

    fun getScreen(screenIndex: Int) = autoplayController.screensList[screenIndex].screen
}