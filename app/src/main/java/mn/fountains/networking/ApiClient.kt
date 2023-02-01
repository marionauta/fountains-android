package mn.fountains.networking

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import java.net.URL

class ApiClient(val baseUrl: URL) {
    @OptIn(ExperimentalSerializationApi::class)
    val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                explicitNulls = false
            })
        }
    }

    suspend inline fun <reified T> get(route: String): T? {
        return try {
            val response = client.get(baseUrl) {
                url {
                    appendEncodedPathSegments(route)
                }
            }
            response.body()
        } catch (exception: Exception) {
            null
        }
    }
}
