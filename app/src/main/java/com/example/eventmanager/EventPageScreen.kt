package com.example.eventmanager

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

@Composable
fun EventPageScreen(
    eventsForPage: List<Event>,
    snackbarHostState: SnackbarHostState,
    index: String?
) {
    val coroutineScope = rememberCoroutineScope()

    LazyColumn {
        items(eventsForPage) { event ->
            ListItem(
                headlineContent = { Text(event.title) },
                supportingContent = { Text(event.location) }, // Add this line
                // listItem is wrapped in pointerInput which allows us to detectGestures
                modifier = Modifier.pointerInput(Unit) {
                    // detect Tap gestures helps us specify action to take when longPress is detected
                    detectTapGestures(
                        onLongPress = {
                            coroutineScope.launch {
//                                event.saved = true
//                                eventDao.update(event)
                                snackbarHostState.showSnackbar(
                                    "Event has been added to itinerary."
                                )
                            }
                        }
                    )
                }
            )
            Divider()
        }
    }
}
