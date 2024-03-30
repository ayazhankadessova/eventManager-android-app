package com.example.eventmanager

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.IconButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.eventmanager.ui.theme.EventManagerTheme
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable





@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventScreen(response: Response, navController: NavHostController) {
    val eventsNum = response.total?.div(3)
    val padding = 16.dp
    var expanded by remember { mutableStateOf(false) }

    Column {
        if (eventsNum != null) {
            repeat(eventsNum) { index ->
                ListItem(
                    headlineContent = { Text("$index") },
                    // go to event page
                    modifier = Modifier.clickable {
                        navController.navigate("event/${index}")
                    },
//                    leadingContent = {
//                        Icon(
//                            Icons.Filled.ThumbUp,
//                            contentDescription = null
//                        )
//                    }
                )
            }
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