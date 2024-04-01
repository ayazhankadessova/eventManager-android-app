package com.example.eventmanager

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class LoginViewModel: ViewModel() {
    val loggedIn = mutableStateOf(false)
    val userId = "660a5c14332f9df622cbac0a"

    open fun logIn() {
        // Update this with your actual login logic
        loggedIn.value = true
    }

    fun logOut() {
        // Update this with your actual logout logic
        loggedIn.value = false
    }
}