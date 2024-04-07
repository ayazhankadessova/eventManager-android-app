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
data class LoginResponse(val token: String)

@Serializable
data class RegistrationResponse(
    val id: IdResponse
)

@Serializable
data class IdResponse(
    val acknowledged: Boolean,
    val insertedId: String
)

@Serializable
data class RegistrationRequest(
    val email: String,
    val password: String,
    val name: String,
    val contact: String,
    val age_group: String,
    val about: String,
    val terms: String
)



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
    suspend fun getEvents(page: Int): Response {
        try {
            return httpClient.get("https://comp4107-spring2024.azurewebsites.net/api/events?page=$page&highlight=true")
                .body<Response>()// Access the list of events from the parsed Response object
        } catch (e: Exception) {
            // Log the exception for better debugging
            // ...
//            throw e // Re-throw the exception for caller to handle
            return Response(listOf<Event>(), null, null, null)
        }
    }

    suspend fun getEventsLocation(page: Int, location: String?): Response {
        try {
            return httpClient.get("https://comp4107-spring2024.azurewebsites.net/api/events/?page=$page&location=$location")
                .body<Response>()// Access the list of events from the parsed Response object
        } catch (e: Exception) {
            // Log the exception for better debugging
            // ...
//            throw e // Re-throw the exception for caller to handle
            return Response(listOf<Event>(), null, null, null)
        }
    }

    suspend fun getEventsSearch(query : String, page: Int): Response {
        return try {
//            Log.i("[Get My Events] userId", id)
            val response: Response = httpClient.get("https://comp4107-spring2024.azurewebsites.net/api/events/?page=$page&search=$query")
                .body<Response>()// Access the list of events from the parsed Response object
            Log.i("[Get Event Search] userId", response.toString())
            return response
        } catch (e: Exception) {
            // Log the exception for better debugging
            // ...
//            throw e // Re-throw the exception for caller to handle
            return Response(listOf<Event>(), null, null, null)
        }
    }

    suspend fun getMyEvents(id : String): ResponseNew? {
        return try {
            Log.i("[Get My Events] Token", token)
            Log.i("[Get My Events] userId", id)
            val responseNew = httpClient.get("https://comp4107-spring2024.azurewebsites.net/api/volunteers/$id/events")
                .body<ResponseNew>()// Access the list of events from the parsed Response object
            Log.i("REPONSE New", responseNew.toString())
            responseNew
        } catch (e: Exception) {
            // Log the exception for better debugging
            // ...
            null // Re-throw the exception for caller to handle
        }
    }


    suspend fun joinEvent(eventId : String, userId: String): String {
        try {

            //        val loginRequest = LoginRequest(email, password)
            Log.i("[Join Event] eventId:", eventId)
            Log.i("[Join Event] userId", userId)
            val response: HttpResponse =
                httpClient.post("https://comp4107-spring2024.azurewebsites.net/api/events/$eventId/volunteers/") {
                }

            Log.i("[Join Event]", response.body())

            return if (response.status == HttpStatusCode.OK) {
                // event if join twice, ok

                "Event Joined."

            } else {

                "Error: unavailable or full quota!"

            }
        } catch (e: Exception) {
            // Log the exception for better debugging
            return "Error: unavailable or full quota!" // Re-throw the exception for caller to handle
        }

    }

    suspend fun unRegister(eventId : String, userId: String): String {
        try {
            //        val loginRequest = LoginRequest(email, password)
            Log.i("[Unregister] eventId:", eventId)
            Log.i("[Unregister] userId", userId)
            val response: HttpResponse =
                httpClient.delete("https://comp4107-spring2024.azurewebsites.net/api/events/$eventId/volunteers/") {
                }

            Log.i("[Unregister]", response.body())

            return if (response.status == HttpStatusCode.OK) {

                "Unregistered"

            } else {

                "Error!"
            }
        } catch (e: Exception) {
            // Log the exception for better debugging
            return "Error!" // Re-throw the exception for caller to handle
        }

    }

    suspend fun getEvent(id: String?): Event? {
        return try {
            httpClient.get("https://comp4107-spring2024.azurewebsites.net/api/events/$id")
                .body()// Access the list of events from the parsed Response object
        } catch (e: Exception) {
            null // Re-throw the exception for caller to handle
        }
    }


    suspend fun register(email: String, password: String, name: String, contact:String, age_group: String, about: String, terms:Boolean): String? {
        try {
            val response: HttpResponse = httpClient.post("https://comp4107-spring2024.azurewebsites.net/api/volunteers/") {
                contentType(ContentType.Application.Json)
                setBody(RegistrationRequest(email,password, name, contact, age_group, about, terms.toString()))
            }

            Log.i("[Register]", response.toString())

            return if (response.status == HttpStatusCode.Created ) {
                val insertedId: String = response.body<RegistrationResponse>().id.insertedId
                Log.i("insertedId", insertedId)
                insertedId
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("[Register]", "Error during registration.", e)
            return null
        }
    }

    suspend fun login(email: String, password: String): String? {
        try {
            val response: HttpResponse = httpClient.post("https://comp4107-spring2024.azurewebsites.net/api/login/") {
                contentType(ContentType.Application.Json)
                setBody(LoginRequest(email,password))
            }

            return if (response.status == HttpStatusCode.OK) {
                val tokenSet: String = response.body<LoginResponse>().token
                token = tokenSet
                Log.i("TOKEEEN", token)
                tokenSet
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("[Login]", "Error during login", e)
            return null
        }
    }


}

