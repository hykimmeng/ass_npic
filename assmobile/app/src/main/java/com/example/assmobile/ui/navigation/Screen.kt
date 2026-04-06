package com.example.assmobile.ui.navigation

sealed class Screen(val route: String) {
    data object Welcome : Screen("welcome")
    data object Login : Screen("login")
    data object Register : Screen("register")
    /** After login: logo + two action buttons. */
    data object Home : Screen("home")
    data object StudentRegistration : Screen("student_registration")
    data object StudentScoreEntry : Screen("student_score_entry")
}
