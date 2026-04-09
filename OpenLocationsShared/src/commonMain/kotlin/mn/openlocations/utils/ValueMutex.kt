package mn.openlocations.utils

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal class ValueMutex<Value>(private val value: Value) {
    private val mutex = Mutex()

    suspend inline fun <Result> withLock(owner: Any, action: (Value) -> Result): Result {
        return mutex.withLock(owner) {
            return@withLock action(value)
        }
    }
}
