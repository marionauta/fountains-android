package mn.openlocations.navigation

sealed class AppScreen(
    val route: String,
) {
    data object Map : AppScreen("map")
}
