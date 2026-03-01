package mn.openlocations.data.models

// Needs to be a class for iOS interop
sealed class OsmId {
    abstract val id: String

    companion object {
        fun fromString(string: String): OsmId? {
            val parts = string.split(':', limit = 2)
            if (parts.size != 2) {
                println("OsmId::fromString invalid argument")
                return null
            }
            return when (parts[0]) {
                "node" -> Node(parts[1])
                "way" -> Way(parts[1])
                else -> null
            }
        }
    }

    data class Node(override val id: String) : OsmId() {
        override fun toString(): String = "node:$id"
    }
    data class Way(override val id: String) : OsmId() {
        override fun toString(): String = "way:$id"
    }
}
