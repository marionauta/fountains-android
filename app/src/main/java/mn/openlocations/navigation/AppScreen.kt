package mn.openlocations.navigation

sealed class AppScreen(
    val route: String,
) {
    object Map : AppScreen("map")
}
