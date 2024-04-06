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
    val page = 1

    Column(modifier = Modifier.verticalScroll(scrollState)) {
        repeat(9) { index ->  // Repeat for numbers 0 to 9
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