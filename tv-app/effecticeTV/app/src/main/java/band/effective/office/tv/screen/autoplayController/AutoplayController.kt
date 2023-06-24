package band.effective.office.tv.screen.autoplayController

import androidx.lifecycle.viewModelScope
import band.effective.office.tv.screen.autoplayController.model.AutoplayState
import band.effective.office.tv.screen.autoplayController.model.OnSwitchCallbacks
import band.effective.office.tv.screen.autoplayController.model.ScreenState
import band.effective.office.tv.screen.navigation.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AutoplayController {
    private var mutableState: MutableStateFlow<AutoplayState> =
        MutableStateFlow(AutoplayState.default)
    val state = mutableState.asStateFlow()
    private var error = false
    private var callbacksMap = mutableMapOf<Screen, OnSwitchCallbacks>()

    fun start(scope: CoroutineScope) {
        checkAutoplayState(scope)
    }

    private fun checkAutoplayState(scope: CoroutineScope) = scope.launch {
        state.collect { controllerState ->
            val currentScreen = controllerState.screensList[controllerState.currentScreenNumber]
            if (controllerState.screensList.isEmpty()) return@collect
            if (state.value.screenState.isForwardDirection)
                callbacksMap[currentScreen]?.onForwardSwitch(state.value)
            else
                callbacksMap[currentScreen]?.onBackSwitch(state.value)
        }
    }

    fun registerScreen(screen: Screen) {
        mutableState.update { it.copy(screensList = it.screensList + screen) }
        checkData()
    }

    fun addCallbacks(screen: Screen, callbacks: OnSwitchCallbacks) {
        callbacksMap[screen] = callbacks
    }

    private fun checkData() {
        mutableState.update {
            when {
                error -> it.copy(isError = true, isLoading = false)
                else -> it.copy(isData = true, isLoading = false)
            }
        }
    }

    fun addError(screen: Screen) {
        mutableState.update {
            it.copy(
                screensList = it.screensList - screen,
                currentScreenNumber = if (it.currentScreenNumber >= it.screensList.size) it.screensList.size - 1 else it.currentScreenNumber
            )
        }
        if (state.value.screensList.size == 0) error = true
        checkData()
    }

    fun nextScreen(screenState: ScreenState) {
        if (state.value.screensList.size <= 1) return
        val currentScreen = state.value.screensList[state.value.currentScreenNumber]
        callbacksMap[currentScreen]?.onLeave()
        mutableState.update {
            it.copy(
                currentScreenNumber = (it.currentScreenNumber + 1) % it.screensList.size,
                screenState = screenState
            )
        }
    }

    fun prevScreen(screenState: ScreenState) {
        if (state.value.screensList.size <= 1) return
        val currentScreen = state.value.screensList[state.value.currentScreenNumber]
        callbacksMap[currentScreen]?.onLeave()
        mutableState.update {
            it.copy(
                currentScreenNumber = (it.currentScreenNumber + it.screensList.size - 1) % it.screensList.size,
                screenState = screenState
            )
        }
    }

    fun resetController() {
        mutableState.update { AutoplayState.default }
        error = false
    }
}