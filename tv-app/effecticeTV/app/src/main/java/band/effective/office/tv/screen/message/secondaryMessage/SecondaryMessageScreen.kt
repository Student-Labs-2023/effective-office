package band.effective.office.tv.screen.message.secondaryMessage

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SecondaryMessageScreen(viewModel: SecondaryMessageViewModel = hiltViewModel()){
    val state by viewModel.state.collectAsState()
    if (state.isData){
        Column() {
            for (message in state.messageList){
                Text(message)
            }
        }
    }
    else {
        //emptyScreen
    }
}