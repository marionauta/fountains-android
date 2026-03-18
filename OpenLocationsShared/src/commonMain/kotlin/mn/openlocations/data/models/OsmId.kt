package mn.openlocations.data.models

// Needs to be a class for iOS interop
sealed class OsmId {
    abstract val id: String

    data class Node(override val id: String) : OsmId()
    data class Way(override val id: String) : OsmId()
}
