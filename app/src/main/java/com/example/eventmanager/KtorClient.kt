package com.example.eventmanager
import android.util.Log
import androidx.room.Entity
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Entity(tableName = "event")
@Serializable
data class Event(
    val _id: String,
    val title: String,
    val organiser: String,
    val description: String,
    val event_date: String,
    val location: String,
    val image: String,
    val quota: Int,
    val highlight: Boolean,
    val createdAt: String?,
    val modifiedAt: String?,
    val volunteers: List<String> = emptyList() // Make volunteers nullable to handle cases where it might be missing
)


@Serializable
data class Response(
    val events: List<Event>, // This is the list of events
    val total: Int?,
    val perPage: Int?,
    val page: Int?
)

@Serializable
data class ResponseNew(
    val events: List<Event>, // This is the list of events
    val total: Int?
)


@Serializable
data class LoginRequest(val email: String, val password: String)

@Serializable
data class JoinRequest(val userId: String)

@Serializable
data class LoginResponse(val token: String)


object KtorClient {

    private var token: String = ""

    val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            }) // enable the client to perform JSON serialization
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
    suspend fun getEvents(): Response {
        try {
            return httpClient.get("https://comp4107-spring2024.azurewebsites.net/api/events")
                .body<Response>()// Access the list of events from the parsed Response object
        } catch (e: Exception) {
            // Log the exception for better debugging
            // ...
            throw e // Re-throw the exception for caller to handle
        }
    }

    suspend fun getEventsPage(number : Int): Response {
        try {
            return httpClient.get("https://comp4107-spring2024.azurewebsites.net/api/events/?page=$number")
                .body<Response>()// Access the list of events from the parsed Response object
        } catch (e: Exception) {
            // Log the exception for better debugging
            // ...
            throw e // Re-throw the exception for caller to handle
        }
    }

    suspend fun getMyEvents(id : String): ResponseNew {
        try {
            Log.i("[Get My Events] Token", token)
            Log.i("[Get My Events] userId", id)
            val ResponseNew = httpClient.get("https://comp4107-spring2024.azurewebsites.net/api/volunteers/$id/events")
                .body<ResponseNew>()// Access the list of events from the parsed Response object
            Log.i("REPONSE New", ResponseNew.toString())
            return ResponseNew
        } catch (e: Exception) {
            // Log the exception for better debugging
            // ...
            throw e // Re-throw the exception for caller to handle
        }
    }

    suspend fun joinEvent(eventId : String, userId: String) {

        //        val loginRequest = LoginRequest(email, password)
        val response: HttpResponse = httpClient.post("https://comp4107-spring2024.azurewebsites.net/api/events/$eventId/volunteers") {
            contentType(ContentType.Application.Json)
            setBody(JoinRequest(userId))
        }

        Log.i("[Join Event]", response.body())

        return response.body()

//        if (response.status == HttpStatusCode.OK) {
//            val tokenSet: String = response.body<LoginResponse>().token
//            token = tokenSet
//            Log.i("TOKEEEN", token)
//            return tokenSet
//
//        } else {
//
//            return null
//
//        }

    }

    suspend fun getEvent(id: String?): Event {
        try {
            return httpClient.get("https://comp4107-spring2024.azurewebsites.net/api/events/$id")
                .body()// Access the list of events from the parsed Response object
        } catch (e: Exception) {
            // Log the exception for better debugging
            // ...
            throw e // Re-throw the exception for caller to handle
        }
    }


    suspend fun login(email: String, password: String): String? {

//        val loginRequest = LoginRequest(email, password)
        val response: HttpResponse = httpClient.post("https://comp4107-spring2024.azurewebsites.net/api/login/") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest(email,password))
        }

        if (response.status == HttpStatusCode.OK) {
            val tokenSet: String = response.body<LoginResponse>().token
            token = tokenSet
            Log.i("TOKEEEN", token)
            return tokenSet

        } else {

            return null

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


}

