package com.example.eventmanager

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyFeedScreen(eventsForPage: List<Event>, navController: NavHostController) {

    val whiteBorder = Color.White
    LazyColumn {
        items(eventsForPage) { event ->

            Card (
                onClick = { navController.navigate("oneEvent/${event._id}") },
                modifier = Modifier
                    .fillMaxWidth().height(200.dp)
                    .border(
                        width = 5.dp,
                        color = whiteBorder
                    )
            ) {
                Column {
                    AsyncImage(
                        model = event.image,
                        contentDescription = "Home page Picture",
                        modifier = Modifier
                            .fillMaxWidth().aspectRatio(1f)  // Change this to fillMaxWidth
                    )
                }
            }
            Box(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = event.title,
                        style = TextStyle(fontSize = 20.sp),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = event.organiser,
                        style = TextStyle(fontSize = 15.sp, color = Color.Gray),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Divider(modifier = Modifier.padding(16.dp), thickness = 2.dp, color = Color.Gray)
        }
    }
}