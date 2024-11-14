package mn.openlocations.networking

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.http.appendEncodedPathSegments
import io.ktor.http.appendPathSegments
import io.ktor.http.parametersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class ApiClient(val baseUrl: String, logLevel: LogLevel = LogLevel.NONE) {
    val client = HttpClient {
        install(Logging) {
            logger = ApiClientLogger()
            level = logLevel
        }
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

    suspend inline fun form(route: ApiRoute) {
        try {
            client.submitForm(
                url = baseUrl,
                formParameters = parametersOf(route.parameters.mapValues { listOf(it.value) })
            ) {
                url {
                    appendPathSegments(route.route)
                }
                headers {
                    for (header in route.headers) {
                        append(header.key, header.value)
                    }
                }
            }
        } catch (exception: Exception) {
            println(exception.message)
        }
    }
}
