package com.example.eventmanager

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventPage(event: Event) {

    LazyColumn {
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)

            ) {
                Column {
                    AsyncImage(
                        model = event.image,
                        contentDescription = "Event Picture",
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                    )

                }
            }

            Box(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
                Column{
                    Text(
                        text =event.title,
                        style = TextStyle(fontSize = 27.sp),
                        modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)

                    )
                    Text(
                        text = event.organiser,
                        style = TextStyle(fontSize = 18.sp, color = Color.Gray),
                        modifier = Modifier.padding(bottom = 5.dp)
                    )
                    Text(text=event.description, style = TextStyle(fontSize = 20.sp), modifier = Modifier.padding(bottom = 16.dp))

                    Divider()

                    Text(
                        text = "Date: " + event.event_date,
                        modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
                        style = TextStyle(fontSize = 20.sp)

                    )

                    Divider()

                    Text(
                        text = "Location: " + event.location,
                        modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
                        style = TextStyle(fontSize = 20.sp)
                    )

                    Divider()

                    Text(
                        text = "Quota: " + event.quota.toString(),
                        modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
                        style = TextStyle(fontSize = 20.sp)
                    )
                }
            }
        }


    }
}

