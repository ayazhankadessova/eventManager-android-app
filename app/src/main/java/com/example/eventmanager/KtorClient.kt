package com.example.eventmanager

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable

object KtorClient {

    private var token: String = ""

    val httpClient = HttpClient {
        install(ContentNegotiation) {
            json() // enable the client to perform JSON serialization
        }
        install(Logging)
        defaultRequest {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            header("Authorization", "Bearer " + token)
        }
        expectSuccess = true
    }
    // Note that we're using suspend functions to enable asynchronous programming in Kotlin.
// This allows us to write cleaner, more concise code that's easier to read and maintain.
    suspend fun getEvents(): List<Event> {
        try {
            // Consider removing additional parameters if the API doesn't require them.
            // If required, adjust based on API documentation.
            val response = httpClient.get("https://comp4107-spring2024.azurewebsites.net/api/events").body<Response>()
            return response.events
        } catch (e: Exception) {
            // Log the exception for better debugging
//            log.error("Error fetching events", e)
            throw e // Re-throw the exception for caller to handle or provide a more user-friendly error message
        }
    }


    @Serializable
    data class HttpBinResponse(
        val args: Map<String, String>,
        val data: String,
        val files: Map<String, String>,
        val form: Map<String, String>,
        val headers: Map<String, String>,
        val json: String?,
        val origin: String,
        val url: String
    )

    /*
    The X-Amzn-Trace-Id header is commonly used for distributed
    tracing in AWS (Amazon Web Services) applications. It helps track
    requests as they flow through different services and components in a
    distributed system. Storing the X-Amzn-Trace-Id in the token property
    allows you to access and reference it later for any further tracking or debugging purposes.
    After storing the X-Amzn-Trace-Id header, the function returns the string representation
    of the response.
     */

    suspend fun postFeedback(feedback: String): String {

        val response: HttpBinResponse = httpClient.post("https://httpbin.org/post") {
            setBody(feedback)
        }.body()

        return response.headers["X-Amzn-Trace-Id"].toString()
    }
}

