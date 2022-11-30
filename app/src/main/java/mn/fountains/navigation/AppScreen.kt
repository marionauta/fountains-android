package mn.fountains.navigation

sealed class AppScreen(
    val route: String,
) {
    object ServerList : AppScreen("servers")
    object ServerAdd : AppScreen("servers/add")
    object Map : AppScreen("map")
    object FountainDetail : AppScreen("fountain")
}
