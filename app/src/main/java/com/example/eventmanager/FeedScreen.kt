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
fun FeedScreen(
    eventsForPage: Response,
    navController: NavHostController,
    search: Boolean,
    page: Int
) {
    val (searchQuery, setSearchQuery) = remember { mutableStateOf("") }
    val (events, setEvents) = remember { mutableStateOf(eventsForPage.events) }
    val (active, setActive) = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val (totalPagesSearchNew, setTotalPagesSearchNew) = remember {
        mutableStateOf(eventsForPage.total?.div(eventsForPage.perPage ?: 1))
    }
    val (currentPage, setCurrentPage) = remember { mutableStateOf(1) }

    if (search) {
        SearchSection(
            searchQuery = searchQuery,
            setSearchQuery = setSearchQuery,
            active = active,
            setActive = setActive,
            coroutineScope = coroutineScope,
            setEvents = setEvents,
            setTotalPagesSearchNew = setTotalPagesSearchNew
        )
        EventsSection(
            events = events,
            navController = navController,
            totalPagesSearchNew = totalPagesSearchNew,
            currentPage = currentPage,
            setCurrentPage = setCurrentPage,
            coroutineScope = coroutineScope,
            searchQuery = searchQuery
        )
    } else {
        EventsSection(
            events = if (events.isEmpty()) eventsForPage.events else events,
            navController = navController,
            totalPagesSearchNew = eventsForPage.total?.div(eventsForPage.perPage ?: 1),
            currentPage = currentPage,
            setCurrentPage = setCurrentPage,
            coroutineScope = coroutineScope,
            searchQuery = ""
        )
    }
}

@Composable
fun SearchSection(
    searchQuery: String,
    setSearchQuery: (String) -> Unit,
    active: Boolean,
    setActive: (Boolean) -> Unit,
    coroutineScope: CoroutineScope,
    setEvents: (List<Event>) -> Unit,
    setTotalPagesSearchNew: (Int?) -> Unit
) {
    SearchBar(
        modifier = Modifier.fillMaxWidth(),
        query = searchQuery,
        onQueryChange = setSearchQuery,
        active = active,
        placeholder = { Text(text = "Search events") },
        leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Search icon") },
        onActiveChange = setActive,
        onSearch = {
            coroutineScope.launch {
                try {
                    val response = KtorClient.getEventsSearch(searchQuery, 1)
                    setEvents(response.events)
                    setTotalPagesSearchNew(response.total?.div(response.perPage ?: 1))
                } catch (e: Exception) {
                    // Handle the exception
                }
            }
            setActive(false)
        },
        content = {},
        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                Icon(
                    modifier = Modifier.clickable { setSearchQuery("") },
                    imageVector = Icons.Default.Close,
                    contentDescription = "Clear search query"
                )
            }
        }
    )
}

@Composable
fun EventsSection(
    events: List<Event>,
    navController: NavHostController,
    totalPagesSearchNew: Int?,
    currentPage: Int,
    setCurrentPage: (Int) -> Unit,
    coroutineScope: CoroutineScope,
    searchQuery: String
) {
    LazyColumn {
        items(events) { event -> fetchEvent(event, navController) }
        item {
            val totalPages = totalPagesSearchNew ?: return@item
            if (totalPages >= 1) {
                PaginationSection(
                    totalPages = totalPages,
                    currentPage = currentPage,
                    setCurrentPage = setCurrentPage,
                    coroutineScope = coroutineScope,
                    searchQuery = searchQuery
                )
            }
        }
    }
}

@Composable
fun PaginationSection(
    totalPages: Int,
    currentPage: Int,
    setCurrentPage: (Int) -> Unit,
    coroutineScope: CoroutineScope,
    searchQuery: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val startPage = max(1, currentPage - 3)
        if (startPage > 1) {
            Button(onClick = {
                setCurrentPage(1)
                coroutineScope.launch { KtorClient.getEventsSearch(searchQuery, 1).events }
            }) {
                Text("1")
            }
            Text("...")
        }
        val endPage = min(totalPages, currentPage)
        for (i in startPage..endPage) {
            Button(onClick = {
                setCurrentPage(i)
                coroutineScope.launch { KtorClient.getEventsSearch(searchQuery, i).events }
            }) {
                Text("$i")
            }
        }
        if (endPage < totalPages) {
            Text("...")
            Button(onClick = {
                setCurrentPage(totalPages)
                coroutineScope.launch { KtorClient.getEventsSearch(searchQuery, totalPages).events }
            }) {
                Text("$totalPages")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun fetchEvent(event: Event, navController: NavController) {
    Card(
        onClick = { navController.navigate("oneEvent/${event._id}") },
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        Column {
            AsyncImage(
                model = event.image,
                contentDescription = "Home page Picture",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
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