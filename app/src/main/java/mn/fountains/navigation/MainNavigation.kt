package mn.fountains.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import mn.fountains.screens.server.add.AddServerScreen
import mn.fountains.screens.server.list.ServerListScreen

@Composable
fun MainNavigation() {
    val controller = rememberNavController()
    MainNavGraph(controller = controller)
}

@Composable
private fun MainNavGraph(controller: NavHostController) {
    NavHost(
        navController = controller,
        startDestination = AppScreen.ServerList.route,
    ) {
        composable(AppScreen.ServerList.route) {
            ServerListScreen(controller)
        }
        composable(AppScreen.ServerAdd.route) {
            AddServerScreen(controller)
        }
    }
}
