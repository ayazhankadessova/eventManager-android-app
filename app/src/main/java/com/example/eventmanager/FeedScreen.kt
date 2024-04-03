package com.example.eventmanager

import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.fillMaxWidth
import java.lang.Math.max
import java.lang.Math.min
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
//import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.runtime.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(eventsForPage: Response, navController: NavHostController, search:Boolean, page:Int) {

    var searchQuery by remember { mutableStateOf("") }
    var events by remember { mutableStateOf(eventsForPage.events) }
    var active by remember { mutableStateOf(false) } // Active state for SearchBar
    val coroutineScope = rememberCoroutineScope()

    if (search) {

        Column {
            SearchBar(
                modifier = Modifier.fillMaxWidth(),
                query = searchQuery,
                onQueryChange = { newQuery -> searchQuery = newQuery },
                active = active,
                placeholder = { Text(text = "Search events") },
                leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Search icon") },
                onActiveChange = {
                    active = it
                },
                onSearch = {

                    coroutineScope.launch {
                        try {
                            val response = KtorClient.getEventsSearch(searchQuery, page)
                            // Update the list of events with the result from the API
                            events = response.events
                        } catch (e: Exception) {
                            // Handle the exception
                        }
                    }

                    active = false
                },
                content = {},
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        Icon(
                            modifier = Modifier.clickable { searchQuery = "" },
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear search query"
                        )
                    }
                }
            )

            LazyColumn {
                items(events) { event ->

                    Card (
                        onClick = { navController.navigate("oneEvent/${event._id}") },
                        modifier = Modifier
                            .fillMaxWidth().height(200.dp)
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
                    Box(modifier = Modifier.fillMaxWidth().padding(15.dp)) {
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
                }
                item {
                    val totalPages = eventsForPage.total?.div(eventsForPage.perPage!!)
                    if (totalPages != null) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val startPage = max(1, page - 2)
                            val endPage = min(totalPages, page + 2)
                            for (i in startPage..endPage) {
                                Button(onClick = { coroutineScope.launch { events = KtorClient.getEventsSearch(searchQuery, i).events } }) {
                                    Text("$i")
                                }
                            }
                            if (endPage < totalPages) {
                                Text("...")
                                Button(onClick = { coroutineScope.launch { events = KtorClient.getEventsSearch(searchQuery, totalPages).events } }) {
                                    Text("$totalPages")
                                }
                            }
                        }
                    }
                }
            }
        }

    } else {
        LazyColumn {
            if (events.isEmpty()) {
                events = eventsForPage.events
            }
            items(events) { event ->

                Card (
                    onClick = { navController.navigate("oneEvent/${event._id}") },
                    modifier = Modifier
                        .fillMaxWidth().height(200.dp)
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
                Box(modifier = Modifier.fillMaxWidth().padding(15.dp)) {
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

            }
            item {
                var totalPages = eventsForPage.total?.div(eventsForPage.perPage!!)
                if (totalPages != null) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val startPage = max(1, page - 2)
                        val endPage = min(totalPages!!, page + 2)
                        for (i in startPage..endPage) {
                            Button(
                                onClick = {
                                    coroutineScope.launch {
                                        val response : Response = KtorClient.getEvents(i)
                                        events = response.events
                                        totalPages = response.total?.div(response.perPage!!)
                                    }
                                }) {
                                Text("$i")
                            }
                        }
                        if (endPage < totalPages!!) {
                            Text("...")
                            Button(onClick = { coroutineScope.launch { events = KtorClient.getEvents(
                                totalPages!!
                            ).events } }) {
                                Text("$totalPages")
                            }
                        }
                    }
                }
            }
        }

        }
    }

