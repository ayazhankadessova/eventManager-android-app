//package com.example.infoday
//
////import androidx.test.platform.app.InstrumentationRegistry
////import androidx.test.ext.junit.runners.AndroidJUnit4
//
//import androidx.compose.ui.test.SemanticsNodeInteraction
//import androidx.compose.ui.test.assertCountEquals
//import androidx.compose.ui.test.assertIsDisplayed
//import androidx.compose.ui.test.junit4.createComposeRule
//import androidx.compose.ui.test.onAllNodesWithText
//import androidx.compose.ui.test.onNodeWithTag
//import androidx.compose.ui.test.onNodeWithText
//import androidx.compose.ui.test.performClick
//import androidx.compose.ui.test.performTouchInput
//import androidx.test.espresso.Espresso
//import androidx.compose.ui.test.longClick
//import androidx.compose.ui.test.performTextInput
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import com.example.eventmanager.LoginViewModel
//import com.example.eventmanager.ScaffoldScreen
//import org.junit.Test
//import org.junit.runner.RunWith
//
//import org.junit.Assert.*
//import org.junit.Rule
//
///**
// * Instrumented test, which will execute on an Android device.
// *
// * See [testing documentation](http://d.android.com/tools/testing).
// */
//@RunWith(AndroidJUnit4::class)
//class MyUiTest {
//    @get:Rule
//    val composeTestRule = createComposeRule()
//
//    @Test
//    fun verifyJoinEventSaved() {
//
//        // Set the content of the screen
//        composeTestRule.setContent {
//            ScaffoldScreen(LoginViewModel())
//        }
//
//        composeTestRule.onNodeWithText("Login").assertIsDisplayed().performClick()
//        // Perform UI interactions and assertions
//        composeTestRule.onNodeWithText("Email").performTextInput("ayazhan@gmail.com")
//        composeTestRule.onNodeWithText("Password").performTextInput("1234")
//
//        composeTestRule.onNodeWithText("login").assertIsDisplayed().performClick()
//
//        composeTestRule.onNodeWithText("Events").assertIsDisplayed().performClick()
//        composeTestRule.onNodeWithText("1").assertIsDisplayed().performClick()
//        /* there will be list of events, click any */
//        composeTestRule.onNodeWithText("productize B2B infrastructures").assertIsDisplayed()
//            .performClick()
//        composeTestRule.onNodeWithText("Join").assertIsDisplayed().performClick()
//        composeTestRule.onNodeWithText("User").assertIsDisplayed().performClick()
//
//        composeTestRule.onNodeWithText("productize B2B infrastructures", substring = true)
//            .assertIsDisplayed()
//    }
//}
////
////    @Test
////    fun verifyBackButton() {
////        composeTestRule.setContent {
////            ScaffoldScreen()
////        }
////
////        composeTestRule.onNodeWithText("Events").assertIsDisplayed().performClick()
////        composeTestRule.onNodeWithText("Comp", substring = true).assertIsDisplayed().performClick()
////        composeTestRule.onNodeWithText("MindDrive", substring = true).assertIsDisplayed()
////
////        // Simulate pressing the back button
////        Espresso.pressBack()
////
////        composeTestRule.onNodeWithText("Comm", substring = true).assertIsDisplayed().performClick()
////        composeTestRule.onNodeWithText("Talk", substring = true).assertIsDisplayed()
////    }
////
////    @Test
////    fun verifyTelCount() {
////        composeTestRule.setContent {
////            ScaffoldScreen()
////        }
////
////        composeTestRule.onNodeWithTag("Info").assertIsDisplayed().performClick()
////
////        // Count the number of nodes with text "3411"
////        composeTestRule.onAllNodesWithText("3411", substring = true).assertCountEquals(3)
////    }
////
////
////}
////
////
////
