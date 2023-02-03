package mn.openlocations.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import mn.openlocations.screens.map.MapScreen
import mn.openlocations.screens.server.add.AddServerScreen
import mn.openlocations.screens.server.list.ServerListScreen
import java.net.URL
import java.net.URLDecoder

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
        composable(
            route = AppScreen.Map.route + "/{url}",
            arguments = listOf(navArgument(name = "url") {
                type = NavType.StringType
            }),
        ) { entry ->
            val decoded = URLDecoder.decode(entry.arguments?.getString("url"), "utf-8")
            val url = URL(decoded)
            MapScreen(url = url, navController = controller)
        }
    }
}