package band.effective.office.tv

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.tv.foundation.lazy.list.TvLazyRow
import band.effective.office.tv.domain.LatestEventInfoUiState
import band.effective.office.tv.domain.models.Employee.EmployeeInfo
import band.effective.office.tv.repository.EmployeeInfoRepositoryImpl
import band.effective.office.tv.source.EmployeeInfoRemoteDataSource
import band.effective.office.tv.useCases.EmployeeInfoUseCase
import band.effective.office.tv.view.viewmodel.EventStoryViewModel
import com.example.effecticetv.ui.theme.EffecticeTVTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EffecticeTVTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    BirthdaysSceen()
                }
            }
        }
    }
}

@Composable
fun TemporaryScreen() {
    TvLazyRow(
        contentPadding = PaddingValues(16.dp),
        userScrollEnabled = true
    ) {
        items(100) {
            Image(
                modifier = Modifier.clickable { },
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = ""
            )
            Spacer(modifier = Modifier.width(32.dp))
        }
    }
}

@Composable
fun BirthdaysSceen() {
    val viewModel = EventStoryViewModel(
        EmployeeInfoUseCase(
            EmployeeInfoRepositoryImpl(
                EmployeeInfoRemoteDataSource()
            )
        )
    )
    LaunchedEffect("birthdayScreenKey") {
        viewModel.uiState.collect {
            viewModel.uiState.collect { state ->
                when (state) {
                    is LatestEventInfoUiState.Success -> showUi(state.employeeInfos)
                    is LatestEventInfoUiState.Error -> Log.d(
                        "BirthdayScreen",
                        "Error occurred ${state.exception}"
                    )
                }
            }
        }
    }
}

private fun showUi(employeeInfos: List<EmployeeInfo>) {
    //Need to write ui logic here
}

