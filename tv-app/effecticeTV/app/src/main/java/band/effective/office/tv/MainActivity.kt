package band.effective.office.tv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import band.effective.office.tv.screen.message.primaryMessage.PrimaryMessageScreen
import band.effective.office.tv.screen.message.secondaryMessage.SecondaryMessageScreen
import band.effective.office.tv.screen.navigation.NavigationHost
import band.effective.office.tv.ui.theme.EffectiveTVTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EffectiveTVTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    PrimaryMessageScreen {
                        SecondaryMessageScreen()
                    //NavigationHost(navController)
                    }
                }
            }
        }
    }
}
