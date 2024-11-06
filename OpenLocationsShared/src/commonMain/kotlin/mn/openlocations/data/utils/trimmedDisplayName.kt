package mn.openlocations.data.utils

fun trimmedDisplayName(displayName: String): String {
    return displayName
        .split(",")
        .map(String::trim)
        .filter { !it.all(Char::isDigit) }
        .getOrNull(0) ?: displayName
}
