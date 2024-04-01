package com.example.eventmanager

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.ListItem
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.launch

import androidx.compose.material3.Text
import androidx.navigation.NavController

@Composable
fun EventPageScreen(
    eventsForPage: List<Event>,
    navController : NavController,
    index: String?
) {
    LazyColumn {
        items(eventsForPage) { event ->
            ListItem(
                headlineContent = { Text(event.title) },
                supportingContent = { Text(event.location) }, // Add this line
                // listItem is wrapped in pointerInput which allows us to detectGestures
                modifier = Modifier.clickable {
                    navController.navigate("oneEvent/${event._id}")
                },
            )
            Divider()
        }
    }
}
