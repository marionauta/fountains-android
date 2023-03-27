package mn.openlocations.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import mn.openlocations.screens.map.MapScreen
import mn.openlocations.screens.area.add.AddAreaScreen
import mn.openlocations.screens.area.list.AreaListScreen

@Composable
fun MainNavigation() {
    val controller = rememberNavController()
    MainNavGraph(controller = controller)
}

@Composable
private fun MainNavGraph(controller: NavHostController) {
    NavHost(
        navController = controller,
        startDestination = AppScreen.AreaList.route,
    ) {
        composable(AppScreen.AreaList.route) {
            AreaListScreen(controller)
        }
        composable(AppScreen.AreaAdd.route) {
            AddAreaScreen(controller)
        }
        composable(
            route = AppScreen.Map.route + "/{id}",
            arguments = listOf(navArgument(name = "id") {
                type = NavType.StringType
            }),
        ) { entry ->
            val id = entry.arguments?.getString("id")!!
            MapScreen(id = id, navController = controller)
        }
    }
}
