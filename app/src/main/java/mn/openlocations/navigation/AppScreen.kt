package mn.openlocations.navigation

sealed class AppScreen(
    val route: String,
) {
    object ServerList : AppScreen("servers")
    object ServerAdd : AppScreen("servers/add")
    object Map : AppScreen("map")
}
