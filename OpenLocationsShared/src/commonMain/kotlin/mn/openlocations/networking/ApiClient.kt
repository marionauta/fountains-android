package mn.openlocations.networking

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.BrowserUserAgent
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.http.HttpStatusCode
import io.ktor.http.appendPathSegments
import io.ktor.http.parametersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import mn.openlocations.domain.models.Url
import mn.openlocations.domain.models.build

class ApiClient(val baseUrl: Url, logLevel: LogLevel = LogLevel.NONE) {
    val client = HttpClient {
        BrowserUserAgent()
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
        install(HttpRequestRetry) {
            retryIf { _, response ->
                if (response.status == HttpStatusCode.TooManyRequests) return@retryIf false
                return@retryIf response.status.value >= 400
            }
            exponentialDelay()
        }
    }

    suspend inline fun <reified T> get(route: ApiRoute): T? {
        return try {
            val response = client.get(baseUrl.build(route).toString()) {
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
                url = baseUrl.toString(),
                formParameters = parametersOf(route.parameters.mapValues { listOf(it.value.toString()) })
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
