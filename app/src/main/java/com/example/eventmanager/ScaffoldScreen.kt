package com.example.eventmanager
import RegistrationForm
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import android.util.Log;
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

//import com.example.infoday.ui.theme.InfoDayTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldScreen() {
    val snackbarHostState = remember { SnackbarHostState() }

    val navController = rememberNavController()
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("Home", "Events", "Search", "Login")

    val feeds by produceState(
        initialValue = Response(listOf<Event>(), null, null, null),
        producer = {
            value = KtorClient.getEvents()
        }
    )

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            // TopAppBar
            CenterAlignedTopAppBar(
                modifier = Modifier.padding(horizontal = 16.dp),
                title = {
                    Text("HKBU InfoDay App")
                }
            )
        },
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(
                            Icons.Outlined.Favorite, contentDescription = item, tint = colorResource(
                                id = (R.color.purple_200)
                            )
                        ) },
                        modifier = Modifier.testTag(item),
                        label = { Text(item) },
                        selected = selectedItem == index,
                        // navigate to a new destination, clear all previous destinations from the back stack (while saving their state), and ensure that only one instance of each destination exists on the back stack.
                        onClick = {
                            selectedItem = index
                            // navController.navigate("Events")
                            navController.navigate(items[selectedItem]) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding),
            ) {

                NavHost(
                    navController = navController,
                    startDestination = "home",
                ) {
                    // mapping of routes and what screens will be shown
                    composable("home") { FeedScreen(response = feeds) }
                    composable("events"){ EventScreen(feeds, navController)}
//

                    composable("event/{index}") { backStackEntry ->
                        val index = backStackEntry.arguments?.getString("index")?.toIntOrNull()
                        if (index != null) {
                            var eventsForPage by remember { mutableStateOf(listOf<Event>()) }
                            LaunchedEffect(index) {
                                eventsForPage = KtorClient.getEventsPage(index).events
                            }
                            EventPageScreen(eventsForPage, navController, index.toString())
                        } else {
                            // Handle the case where index is null
                        }
                    }
                    composable("oneEvent/{_id}") { backStackEntry ->
                        val eventId = backStackEntry.arguments?.getString("_id")
                        Log.i("Event id is null" , backStackEntry.arguments.toString())
                        if (eventId != null) {

                            var event by remember { mutableStateOf<Event?>(null) }
                            LaunchedEffect(eventId) {
                                event = KtorClient.getEvent(eventId)
                            }
                            event?.let { EventPage(event!!) }
                        } else {
                            // Handle the case where eventId is null
                            Log.i("Event id is null" ," NULL");

                        }
                    }
                    composable("search") { HomeScreen() }
                    composable("login") { LoginForm(navController) }
                    composable("registrationPage") { RegistrationForm()}
                }
            }
        }
    )
}

//@Preview(showBackground = true)
//@Composable
//fun ScaffoldScreenPreview() {
//    InfoDayTheme {
//        ScaffoldScreen()
//    }
//}