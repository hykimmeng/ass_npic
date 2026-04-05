package com.example.assmobile.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Home : Screen("home")
    object RegisterStudent : Screen("register_student")
    object EnterScore : Screen("enter_score")
    object SchoolInfo : Screen("school_info")
}
