package mn.openlocations.data.models

// Needs to be a class for iOS interop
sealed class OsmId {
    abstract val id: String

    data class Node(override val id: String) : OsmId() {
        override fun toString(): String = "node:$id"
    }

    data class Way(override val id: String) : OsmId() {
        override fun toString(): String = "way:$id"
    }

    companion object {
        fun from(value: String): OsmId? {
            val parts = value.split(":", limit = 2)
            if (parts.size < 2) return null
            return when (parts[0]) {
                "node" -> Node(parts[1])
                "way" -> Way(parts[1])
                else -> null
            }
        }
    }
}
