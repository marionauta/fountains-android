package mn.openlocations.data.routes

import kotlin.jvm.JvmInline

@JvmInline
internal value class OverpassFilter(private val value: Map<String, String>) {
    constructor(vararg pairs: Pair<String, String>) : this(mapOf(*pairs))

    // format: nw[key1=value1][key2=value2];
    override fun toString(): String {
        return "nw${value.map { "[${it.key}=${it.value}]" }.joinToString(separator = "")};"
    }
}
