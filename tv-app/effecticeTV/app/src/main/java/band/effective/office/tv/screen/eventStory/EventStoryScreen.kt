package band.effective.office.tv.screen.eventStory

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import band.effective.office.tv.R
import band.effective.office.tv.screen.components.EventInfo

@Composable
fun EventStoryScreen(viewModel: EventStoryViewModel) {
    val context = LocalContext.current
    val errorMessage = stringResource(id = R.string.error_occurred)

    val state by viewModel.uiState.collectAsState()
    when {
        state.isLoading -> LoadScreen()
        state.isError -> {
            showErrorMessage(context, errorMessage + state.errorText)
            Log.d("EventStoryScreen", errorMessage + state.stackTrace)
        }
        state.isLoaded -> {
            state.eventsInfo.map {
                Log.d("EventStoryScreenTest", it.name)
            }
            Log.d(
                "EventStoryScreenTest",
                "Current index is - " + state.currentStoryIndex.toString()
            )
            EventInfo(state.eventsInfo, state.currentStoryIndex)
        }
    }

}

private fun showErrorMessage(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}

private fun LoadScreen() {

}