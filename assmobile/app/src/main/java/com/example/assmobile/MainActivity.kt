package com.example.assmobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.assmobile.ui.navigation.Screen
import com.example.assmobile.ui.screens.LoginScreen
import com.example.assmobile.ui.screens.PostLoginHomeScreen
import com.example.assmobile.ui.screens.RegisterScreen
import com.example.assmobile.ui.screens.StudentRegistrationScreen
import com.example.assmobile.ui.screens.StudentScoreEntryScreen
import com.example.assmobile.ui.screens.WelcomeScreen
import com.example.assmobile.ui.theme.AssmobileTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AssmobileTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Welcome.route
    ) {
        composable(Screen.Welcome.route) {
            WelcomeScreen(
                onContinue = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }
        composable(Screen.Register.route) {
            RegisterScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Home.route) {
            PostLoginHomeScreen(
                usernameHint = SessionStore.displayName,
                onStudentRegistration = {
                    navController.navigate(Screen.StudentRegistration.route)
                },
                onStudentScores = {
                    navController.navigate(Screen.StudentScoreEntry.route)
                },
                onLogout = {
                    SessionStore.displayName = null
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.StudentRegistration.route) {
            StudentRegistrationScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.StudentScoreEntry.route) {
            StudentScoreEntryScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
