package mn.openlocations.navigation

sealed class AppScreen(
    val route: String,
) {
    object AreaList : AppScreen("areas")
    object AreaAdd : AppScreen("areas/add")
    object Map : AppScreen("map")
}
