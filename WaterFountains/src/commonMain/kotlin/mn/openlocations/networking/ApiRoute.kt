package mn.openlocations.networking

interface ApiRoute {
    val route: String
    val headers: Map<String, String>
}
