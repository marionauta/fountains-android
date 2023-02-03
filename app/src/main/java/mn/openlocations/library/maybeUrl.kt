package mn.openlocations.library

import java.net.URL

fun maybeUrl(string: String): URL? {
    return try { URL(string) } catch (e: Exception) { null }
}
