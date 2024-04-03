package com.example.eventmanager
import RegistrationForm
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
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
import android.util.Log;
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import java.util.Locale

//import com.example.infoday.ui.theme.InfoDayTheme

@Composable
fun getScreenTitle(navController: NavController, items: List<String>): String {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route?.replaceFirstChar { if (it.isLowerCase()) it.titlecase(
        Locale.getDefault()) else it.toString() }
    if (currentDestination != null) {
        Log.i("Dest route: " , currentDestination)
    }

    if (currentDestination == "Event/{index}") {
        return "Location"
    } else if (currentDestination == "OneEvent/{_id}") {
        return "Event Title"
    } else if (currentDestination == "Search") {
        return "Events"
    } else if (currentDestination == "RegistrationPage") {
        return "Become Volunteer"
    } else if (currentDestination == "User") {
        return "Registered Events"
    }
    val index = items.indexOf(currentDestination)
    return if (index != -1) items[index] else "App"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldScreen(loginViewModel: LoginViewModel) {

    val dataStore = UserPreferences(LocalContext.current)
    val userId by dataStore.getUserId.collectAsState(initial = null)

    var loggedIn:Boolean = loginViewModel.loggedIn.value
//    val loggedIn = loginViewModel.loggedIn.value
    val snackbarHostState = remember { SnackbarHostState() }

    val navController = rememberNavController()
    var selectedItem by remember { mutableIntStateOf(0) }
    var items = if (loggedIn) listOf("Home", "Events", "Search", "User") else listOf("Home", "Events", "Search", "Login")


    val events by produceState(
        initialValue = emptyList<Event>(),
        producer = {
            value = KtorClient.getEvents().events
        }
    )

    val response by produceState(
        initialValue = Response(listOf<Event>(), null, null, null),
        producer = {
            value = KtorClient.getEvents()
        }
    )

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            // TopAppBar
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.secondary,
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Outlined.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                },
                title = {
                    Text(getScreenTitle(navController, items))
                },


            )
        },
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Text(item) },
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
                    composable("home") {
                        selectedItem = 0
                        FeedScreen(events, navController, false)
                    }
                    composable("search") {
                        FeedScreen(events, navController, true)
                    }
                    composable("events"){ EventScreen(response, navController)}
//                    composable("user") { HomeScreen()}
//

                    composable("event/{index}/{page}") { backStackEntry ->
                        val location : String? = backStackEntry.arguments?.getString("index")
                        val page : Int? = backStackEntry.arguments?.getString("page")?.toIntOrNull()
                        backStackEntry.arguments?.toString()?.let { Log.i("index", it) }
                        if (location != null && page !=null) {
                            var eventsForLocRes by remember { mutableStateOf(Response(listOf<Event>(), null, null, null)) }
                            LaunchedEffect(location) {
                                eventsForLocRes = KtorClient.getEventsLocation(page, location)
                            }
                            EventPageScreen(eventsForLocRes, navController, location.toString(), page)
                        } else {
                            // Handle the case where index is null
                        }
                    }
                    composable("oneEvent/{_id}") { backStackEntry ->
                        val eventId = backStackEntry.arguments?.getString("_id")
                        if (eventId != null) {

                            var event by remember { mutableStateOf<Event?>(null) }
                            LaunchedEffect(eventId) {
                                event = KtorClient.getEvent(eventId)
                            }

                            var registered by remember { mutableStateOf<Boolean>(false) }
                            LaunchedEffect(eventId) {
                                registered =
                                    KtorClient.getEvent(eventId)?.volunteers?.contains(userId) ?: false
                            }



                            event?.let { EventPage(event!!, snackbarHostState, loggedIn, registered) }
                        } else {
                            // Handle the case where eventId is null
                            Log.i("Event id is null" ," NULL");

                        }
                    }
                    composable("user") {
//                        val dataStore = UserPreferences(LocalContext.current)
//                        val userId by dataStore.getUserId.collectAsState(initial = null)
//                        Log.i(backStackEntry.arguments?.toString(), "check")
                        if (userId != null) {

                            var eventsForPage by remember { mutableStateOf(listOf<Event>()) }
                            LaunchedEffect(userId) {
                                eventsForPage = KtorClient.getMyEvents(userId!!)?.events ?: emptyList<Event>()
                            }
                            FeedScreen(eventsForPage, navController, false)
                        } else {
                            // Handle the case where eventId is null
                            Log.i("Event id is null" ," NULL");

                        }
                    }

                    composable("login") { LoginForm(navController, snackbarHostState, loginViewModel) }
                    composable("registrationPage") { RegistrationForm(snackbarHostState, navController)}
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