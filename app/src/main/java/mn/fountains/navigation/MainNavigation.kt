package mn.fountains.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import mn.fountains.screens.fountain.FountainDetailScreen
import mn.fountains.screens.map.MapScreen
import mn.fountains.screens.server.add.AddServerScreen
import mn.fountains.screens.server.list.ServerListScreen
import java.net.URL
import java.net.URLDecoder
import java.net.URLEncoder

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
        composable(
            route = AppScreen.FountainDetail.route + "/{id}",
            arguments = listOf(navArgument(name = "id") {
                type = NavType.StringType
            }),
        ) { entry ->
            val id = entry.arguments?.getString("id")!!
            FountainDetailScreen(
                fountainId = id,
                navController = controller,
            )
        }
    }
}
