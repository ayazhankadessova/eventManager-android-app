package com.example.eventmanager

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.auth0.android.jwt.JWT
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventPage(event: Event, snackbarHostState: SnackbarHostState, loggedIn: Boolean, registered : Boolean) {

    val dataStore = UserPreferences(LocalContext.current)
    val userId by dataStore.getUserId.collectAsState(initial = null)
    val coroutineScope = rememberCoroutineScope()

//    var loggedIn:Boolean = userId != null

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
                Column {
                    Text(
                        text = event.title,
                        style = TextStyle(fontSize = 27.sp),
                        modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)

                    )
                    Text(
                        text = event.organiser,
                        style = TextStyle(fontSize = 18.sp, color = Color.Gray),
                        modifier = Modifier.padding(bottom = 5.dp)
                    )
                    Text(
                        text = event.description,
                        style = TextStyle(fontSize = 20.sp),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

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

            if (loggedIn) {
                if (!registered) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Button(
                                onClick = {
                                    coroutineScope.launch {
                                        val res = userId?.let { KtorClient.joinEvent(event._id, it) }

                                        if (res != null) {
                                            snackbarHostState.showSnackbar(res)
                                        }
                                    }
                                },
                                shape = RoundedCornerShape(5.dp),

                                ) {
                                Text("Join Event")
                            }
                        }
                    } // box
                } else {

                    Box(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Button(
                                onClick = {
                                    coroutineScope.launch {
                                        val res = userId?.let { KtorClient.unRegister(event._id, it) }

                                        if (res == "Unregistered" || res == "Error!") {
                                            snackbarHostState.showSnackbar(res)
                                            // check button text to "Unregistered
                                        }
                                    }
                                },
                                shape = RoundedCornerShape(5.dp),

                                ) {
                                Text("Unregister")
                            }
                        }
                    } // box

                }



            } // logged in

        }

    }
    }


