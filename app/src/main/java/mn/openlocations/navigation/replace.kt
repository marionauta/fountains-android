package mn.openlocations.navigation

import androidx.navigation.NavController

fun NavController.replace(route: String) {
    popBackStack()
    navigate(route)
}
