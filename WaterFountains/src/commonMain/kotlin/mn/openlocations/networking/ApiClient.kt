package mn.openlocations.networking

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

class ApiClient(val baseUrl: String) {
    @OptIn(ExperimentalSerializationApi::class)
    val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                explicitNulls = false
            })
        }
    }

    suspend inline fun <reified T> get(route: ApiRoute): T? {
        return try {
            val response = client.get(baseUrl) {
                url {
                    appendEncodedPathSegments(route.route)
                    for (parameter in route.parameters) {
                        this.parameters.append(parameter.key, parameter.value)
                    }
                }
                headers {
                    for (header in route.headers) {
                        append(header.key, header.value)
                    }
                }
            }
            response.body()
        } catch (exception: Exception) {
            println(exception.message)
            null
        }
    }
}
