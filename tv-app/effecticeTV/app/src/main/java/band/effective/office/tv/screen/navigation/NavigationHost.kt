package band.effective.office.tv.screen.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import band.effective.office.tv.screen.autoplay.AutoplayScreen
import band.effective.office.tv.screen.autoplayMenu.AutoplayMenuScreen
import band.effective.office.tv.screen.menu.MenuScreen
import band.effective.office.tv.screen.message.primaryMessage.PrimaryMessageScreen

@Composable
fun NavigationHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Menu.name) {
        composable(Screen.Menu.name) {
            MenuScreen(
                itemsList = NavigationModel.screensList,
                navController = navController
            )
        }
        NavigationModel.screensList.forEach { screen ->
            composable(screen.screen.name) { screen.screenFun() }
        }
        composable(Screen.Autoplay.name) { AutoplayScreen() }
        composable(Screen.AutoplayMenu.name) { AutoplayMenuScreen(navController = navController) }
        composable(Screen.MessageScreen.name) { PrimaryMessageScreen() {

        }}
    }
}