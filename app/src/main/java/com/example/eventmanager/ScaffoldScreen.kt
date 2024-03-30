package com.example.eventmanager
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
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
                    composable("home") { HomeScreen() }
                    composable("events"){ EventScreen(feeds)}
//                    composable("event/{deptId}") { NavbackStackEntry ->
//                        EventScreen(NavbackStackEntry.arguments?.getString("deptId"))
//                    }
                    composable("event/{deptId}") { HomeScreen()
                    }
                    composable("search") { HomeScreen() }
                    composable("login") { HomeScreen() }
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