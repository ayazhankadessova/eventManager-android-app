package com.example.eventmanager

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Divider
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventScreen(navController: NavHostController) {
    val scrollState = rememberScrollState()
    val page:Int = 1

    Column(modifier = Modifier.verticalScroll(scrollState)) {
        repeat(10) { index ->  // Repeat for numbers 0 to 9
            ListItem(
                headlineContent = { Text("${index+1}") },
                // go to event page
                modifier = Modifier.clickable {
                    navController.navigate("event/${index + 1}/${page}")
                },
            )
            Divider()
        }
    }
}

//@Composable
//fun EventScreen(snackbarHostState: SnackbarHostState, deptId: String?) {
//
//    val eventDao = EventDatabase.getInstance(LocalContext.current).eventDao()
////    The events variable represents the current value of the LiveData and is updated whenever there are changes in the observed data.
//    val events by eventDao.getAll().observeAsState(listOf())
//    val coroutineScope = rememberCoroutineScope()
//
//    LazyColumn {
//        items(events) { event ->
//            ListItem(
//                headlineContent = { Text(event.title) },
//                // listItem is wrapped in pointerInput which allows us to detectGestures
//                modifier = Modifier.pointerInput(Unit) {
//                    // detect Tap gestures helps us specify action to tale when longPress is detected
////                    detectTapGestures(
////                        onLongPress = {
////                            coroutineScope.launch {
////                                event.saved = true
////                                eventDao.update(event)
////                                snackbarHostState.showSnackbar(
////                                    "Event has been added to itinerary."
////                                )
////                            }
////                        }
////                    )
//                }
//            )
//            Divider()
//        }
//    }
//}



//@Preview(showBackground = true)
//@Composable
//fun FeedPreview() {
//    EventManagerTheme {
//        EventScreen(Event.data)
//    }
//}