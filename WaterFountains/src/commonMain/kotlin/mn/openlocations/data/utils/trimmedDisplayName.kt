package mn.openlocations.data.utils

fun trimmedDisplayName(displayName: String): String {
    val parts = displayName
        .split(",")
        .map(String::trim)
        .filter { !it.all(Char::isDigit) }
    val selected = if (parts.size <= 3) parts else listOf(
        parts[0],
        parts[parts.size - 2],
        parts[parts.size - 1],
    )
    return selected.joinToString(separator = ", ")
}
