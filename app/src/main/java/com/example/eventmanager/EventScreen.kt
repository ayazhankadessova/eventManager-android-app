package com.example.eventmanager

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.eventmanager.ui.theme.EventManagerTheme
import kotlinx.serialization.Serializable





@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventScreen(response: Response) {
    val eventsNum = response.total?.div(3)
    val padding = 16.dp
    var expanded by remember { mutableStateOf(false) }

    Column {
        if (eventsNum != null) {
            repeat(eventsNum) { index ->
                ListItem(
                    modifier = Modifier
                        .padding(8.dp)
                        .border(
                            1.dp,
                            colorResource(id = R.color.purple_500),
                            shape = RoundedCornerShape(8.dp)
                        ),
                    headlineContent = { Text("Page ${index + 1}") },
                    trailingContent = {
                        IconButton(onClick = { expanded = !expanded }) {
                            Icon(
                                Icons.Filled.ArrowDropDown,
                                contentDescription = null
                            )
                        }
                        if (expanded) {
                            Column {
                                ListItem(
                                    headlineContent = { Text("Page ${index + 1}") }
                                )
                            }
                        }
                    }
                )
            }
        }
    }
}





//@Preview(showBackground = true)
//@Composable
//fun FeedPreview() {
//    EventManagerTheme {
//        EventScreen(Event.data)
//    }
//}