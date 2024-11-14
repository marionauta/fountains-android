package mn.openlocations.domain.utils

interface SecureStringStorage {
    operator fun set(key: String, value: String)
    operator fun get(key: String): String?
}
