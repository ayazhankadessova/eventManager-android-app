package com.example.eventmanager

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
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
import java.lang.Math.max
import java.lang.Math.min
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.runtime.*
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(eventsForPage: Response, navController: NavHostController, search:Boolean, page:Int, searchQueryOld:String) {

    var searchQuery by remember { mutableStateOf("") }
    var events by remember { mutableStateOf(eventsForPage.events) }
    var active by remember { mutableStateOf(false) } // Active state for SearchBar
    val coroutineScope = rememberCoroutineScope()

    var totalPagesSearch = eventsForPage.total?.div(eventsForPage.perPage!!)
    var totalPagesSearchNew by remember { mutableStateOf(totalPagesSearch) }
    var currentPage by remember { mutableStateOf(1) }  // Initialize to 1 (first page)



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
                            totalPagesSearchNew = response.total?.div(response.perPage!!)

                            Log.i("TOTAL PAGES", totalPagesSearchNew.toString())
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
                items(events) {

                    event -> fetchEvent(event, navController)
                }
                item {

                    val totalPages = totalPagesSearchNew ?: return@item
                    Log.i("TOTAL PAGES", totalPages.toString())
                    if (totalPages >=1) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth().padding(10.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            val startPage = max(1, currentPage - 2)
                            if (startPage > 1) {
                                Button(onClick = {
                                    currentPage = 1
                                    coroutineScope.launch { events = KtorClient.getEventsSearch(searchQuery, 1).events } }) {
                                    Text("1")
                                }
                                Text("...")
                            }
                            val endPage = min(totalPages, currentPage + 2)
                            for (i in startPage..endPage) {
                                Button(onClick = {
                                    currentPage = i
                                    coroutineScope.launch { events = KtorClient.getEventsSearch(searchQuery, i).events } }) {
                                    Text("$i")
                                }
                            }
                            if (endPage < totalPages) {
                                Text("...")
                                Button(onClick = {
                                    currentPage = totalPages
                                    coroutineScope.launch { events = KtorClient.getEventsSearch(searchQuery, totalPages).events } }) {
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
            items(events) {
                event -> fetchEvent(event, navController)

            }

            // Pagination here
            if (totalPagesSearch != null) {

                item {
                    Log.i("TOTAL PAGES", totalPagesSearch.toString())
                    if (totalPagesSearch >=1) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth().padding(10.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            val startPage = max(1, currentPage - 2)
                            if (startPage > 1) {
                                Button(onClick = {
                                    currentPage = 1
                                    coroutineScope.launch { events = KtorClient.getEvents(1).events } }) {
                                    Text("1")
                                }
                                Text("...")
                            }
                            val endPage = min(totalPagesSearch, currentPage + 2)
                            for (i in startPage..endPage) {
                                Button(onClick = {
                                    currentPage = i
                                    coroutineScope.launch { events = KtorClient.getEvents(i).events } }) {
                                    Text("$i")
                                }
                            }
                            if (endPage < totalPagesSearch) {
                                Text("...")
                                Button(onClick = {
                                    currentPage = totalPagesSearch
                                    coroutineScope.launch { events = KtorClient.getEvents(totalPagesSearch).events } }) {
                                    Text("$totalPagesSearch")
                                }
                            }
                        }
                    }
                }
            }
        }
      }
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun fetchEvent(event: Event, navController:NavController) {

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