package com.example.assmobile.ui.navigation

sealed class Screen(val route: String) {
    data object Welcome : Screen("welcome")
    data object Login : Screen("login")
    data object Register : Screen("register")
    /** Shell with navigation drawer (dashboard + inner routes). */
    data object Main : Screen("main")

    /** Inner destinations shown inside [com.example.assmobile.ui.navigation.MainDrawerScaffold]. */
    data object Dashboard : Screen("dashboard")
    data object StudentManagement : Screen("student_management")
    data object SchoolInfoDrawer : Screen("school_info_drawer")
}
