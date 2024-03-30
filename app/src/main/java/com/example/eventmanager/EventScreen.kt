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
    val _id: String,
    val title: String,
    val organiser: String,
    val description: String,
    val event_date: String,
    val location: String,
    val image: String,
    val quota: Int,
    val highlight: Boolean,
    val createdAt: String,
    val modifiedAt: String?,
    val volunteers: List<String>?
)

@Serializable
data class Response(
    val events: List<Event>,
    val total: Int
)

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
                        Text("HELLO",
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

//@Preview(showBackground = true)
//@Composable
//fun FeedPreview() {
//    EventManagerTheme {
//        EventScreen(Event.data)
//    }
//}