package com.example.spotfix.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.spotfix.ScreensAdmin.AdminLogin
import com.example.spotfix.ScreensAdmin.Details
import com.example.spotfix.ScreensUser.HomePage
import com.example.spotfix.ScreensUser.LoginScreen
import com.example.spotfix.ScreensUser.Screen
import com.example.spotfix.ScreensUser.OtpScreen
import com.example.spotfix.screens.Dashboard

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(navController)
        }

        composable(
            route = Screen.OTP.route,
            arguments = listOf(
                navArgument("phone") { type = NavType.StringType },
                navArgument("verificationId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val phone = backStackEntry.arguments?.getString("phone") ?: ""
            val verificationId = backStackEntry.arguments?.getString("verificationId") ?: ""
            OtpScreen(
                navController = navController,
                phone = phone,
                verificationId = verificationId
            )
        }

        composable(Screen.Home.route) {
            HomePage()
        }
        composable(Screen.AdminLogin.route) {
            AdminLogin(navController)
        }
        composable(Screen.Dashboard.route) {
            Dashboard(navController)
        }
        composable("details") {
            Details(navController)
        }
    }
}

