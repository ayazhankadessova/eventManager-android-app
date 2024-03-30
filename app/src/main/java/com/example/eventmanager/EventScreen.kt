package com.example.eventmanager

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.eventmanager.ui.theme.EventManagerTheme
import kotlinx.serialization.Serializable

@Serializable
data class Event(
    val id: String, // Changed from Int to handle potential string IDs like in the sample data
    val title: String,
    val organizer: String,
    val description: String,
    val eventDate: String, // Renamed for clarity (consider using a Date object if needed)
    val location: String?,
    val image: String?,
    val quota: Int?,
    val highlight: Boolean,
    val createdAt: String,
    val modifiedAt: String?,
    val volunteers: List<String>?
) {
    companion object {
        const val ERROR_ID = "500"
        val ERROR_IMAGE = "ERROR.jpg"

//        fun isErrorEvent(event: Event): Boolean = event.id == ERROR_ID
        val data = listOf(
    Event(
        "123", // Assuming ID is a string in your actual data
        "test",
"COMP",
        " COMP ",
        "2023-11-23T12:32 ",
        "HKBU",
        "https://picsum.photos/seed/2023-11-23T12:32/800/800",
        6,
        true,
        "2023-11-23T13:29:26.636Z",
        "2023-11-28T03:20:11.704Z",
        listOf("655ddc74cb6fa4000273365e")
    ),
)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventScreen(events: List<Event>) {

//    val lighterBorder = Color.Magenta
    val whiteBorder = Color.White
    LazyColumn {
        items(events) { feed ->

            val isClicked = remember { mutableStateOf(false) }

            Card(
                onClick = { isClicked.value = !isClicked.value },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .border(
                        width = 5.dp,
                        color = if (isClicked.value) Color.Yellow else whiteBorder
                    ),
            ) {
                Column {

                    Box(Modifier.fillMaxSize()) {
                        Text(feed.title,
                            Modifier
                                .align(Alignment.Center)
                                .padding(2.dp))
                    }
                }
            }
            Divider(modifier = Modifier.padding(16.dp), thickness = 2.dp, color = Color.Gray)

//            HorizontalDivider(color = Color.DarkGray)

        }
    }
}

@Preview(showBackground = true)
@Composable
fun FeedPreview() {
    EventManagerTheme {
        EventScreen(Event.data)
    }
}