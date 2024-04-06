package com.example.eventmanager


import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InfoGreeting() {
    val padding = 16.dp

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.size(padding))
        Spacer(Modifier.size(padding))
        Text(text = "HKBU InfoDay App", fontSize = 30.sp)
    }
}


data class Contact(val office: String, val tel: String) {
    companion object {
        val data = listOf(
            Contact(office = "Admission Office", tel = "3411-2200"),
            Contact(office = "Emergencies", tel = "3411-7777"),
            Contact(office = "Health Services Center", tel = "3411-7447")
        )
    }
}


@Composable
fun PhoneList() {

    val ctx = LocalContext.current

    Column {
        Contact.data.forEach { message ->
            ListItem(
                modifier = Modifier.
                padding(8.dp)
                    .border(
                        1.dp,
                        colorResource(id = R.color.purple_500),
                        shape = RoundedCornerShape(8.dp)
                    ).clickable {
                        val u = Uri.parse("tel:" + message.tel)
                        val i = Intent(Intent.ACTION_DIAL, u)
                        ctx.startActivity(i) },
                headlineContent = { Text(message.office) },
                leadingContent = {
                    Icon(
                        Icons.Filled.Call,
                        contentDescription = null
                    )
                },
                trailingContent = { Text(message.tel) }
            )
        }
    }
}





@Preview(showBackground = true)
//@Composable
//fun InfoPreview() {
//    HomeScreen {
//        Column {
//            InfoGreeting()
//            PhoneList()
////            FeedBack(snackbarHostState )
//        }
//    }
//}
//

@Composable
fun HomeScreen() {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.verticalScroll(
        rememberScrollState())) {
        InfoGreeting()
        PhoneList()

    }
}