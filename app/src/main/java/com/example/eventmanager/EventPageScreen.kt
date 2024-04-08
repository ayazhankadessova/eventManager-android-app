package com.example.eventmanager

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ListItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.Text
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@Composable
fun EventPageScreen(
    eventsLocRes: Response,
    navController: NavController,
    location: String?, page: Int
) {
    val totalPages = eventsLocRes.total?.div(eventsLocRes.perPage!!)
    Log.i("PAGESSS", totalPages.toString())

    LazyColumn {
        items(eventsLocRes.events) { event ->
            ListItem(
                headlineContent = { Text(event.title) },
                supportingContent = { Text(event.location) },
                modifier = Modifier.clickable {
                    navController.navigate("oneEvent/${event._id}")
                },
            )
            Divider()
        }

        // Pagination here
        if (totalPages != null) {
            item {
                Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                    val startPage = kotlin.math.max(1, page - 2)
                    val endPage = kotlin.math.min(totalPages, page)
                    for (i in startPage..endPage) {
                        Button(onClick = { navController.navigate("event/${location}/$i") }) {
                            Text("$i")
                        }
                    }
                    if (endPage < totalPages) {
                        Text("...")
                        Button(onClick = { navController.navigate("event/${location}/$totalPages") }) {
                            Text("$totalPages")
                        }
                    }
                }
            }
        }
    }
}
