package mn.openlocations.library

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.delay

// Adapted from https://stackoverflow.com/a/57252799

/**
 * Constructs a function that processes input data and passes it to [destinationFunction] only if
 * there's no new data for at least [waitMs] milliseconds.
 */
fun <T, R> debounce(
    waitMs: Long = 300L,
    coroutineScope: CoroutineScope,
    destinationFunction: suspend (T) -> R
): (T) -> Deferred<R>? {
    var debounceJob: Deferred<R>? = null
    return { param: T ->
        debounceJob?.cancel()
        debounceJob = coroutineScope.async {
            delay(waitMs)
            return@async destinationFunction(param)
        }
        debounceJob
    }
}
