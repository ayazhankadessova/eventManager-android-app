package com.example.eventmanager

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel

class LoginViewModel: ViewModel() {

    val loggedIn = mutableStateOf(false)

    open fun logIn() {
        // Update this with your actual login logic
        loggedIn.value = true
    }

    fun logOut() {
        // Update this with your actual logout logic
        KtorClient.logout()
        loggedIn.value = false
    }
}